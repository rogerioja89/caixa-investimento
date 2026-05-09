package com.github.rogerioja89;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// @QuarkusTest sobe a aplicação completa em modo de teste.
// O banco H2 em memória é usado (configurado em application.properties com prefixo %test).
// O DataSeeder popula os produtos automaticamente ao detectar a tabela vazia.
@QuarkusTest
class SimulacaoResourceTest {

    // Simula um CDB válido: valor e prazo dentro do range do produto "CDB Caixa 2026"
    @Test
    void deveCriarSimulacaoComSucesso() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 123,
                    "valor": 10000.00,
                    "prazoMeses": 12,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(201)
            .body("produtoValidado.tipo", equalTo("CDB"))
            .body("resultadoSimulacao.prazoMeses", equalTo(12))
            .body("resultadoSimulacao.valorFinal", notNullValue())
            .body("dataSimulacao", notNullValue());
    }

    // Quando nenhum produto atende os critérios, deve retornar HTTP 422
    @Test
    void deveRetornar422QuandoNaoHaProdutoElegivel() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 123,
                    "valor": 10000.00,
                    "prazoMeses": 12,
                    "tipoProduto": "DEBENTURE"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(422)
            .body("erro", notNullValue());
    }

    // Campos inválidos devem retornar HTTP 400 com campo "erro" descrevendo as violações
    @Test
    void deveRetornar400QuandoCamposInvalidos() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": -1,
                    "valor": -500,
                    "prazoMeses": 0,
                    "tipoProduto": ""
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(400)
            .body("erro", notNullValue());
    }

    // clienteId ausente no GET deve retornar 400 com campo "erro"
    @Test
    void deveRetornar400QuandoClienteIdAusente() {
        given()
        .when()
            .get("/simulacoes")
        .then()
            .statusCode(400)
            .body("erro", equalTo("O parâmetro clienteId é obrigatório"));
    }

    // Após criar uma simulação, o histórico deve conter o registro
    @Test
    void deveBuscarHistoricoDoCliente() {
        Long clienteId = 999L;

        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 999,
                    "valor": 5000.00,
                    "prazoMeses": 6,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(201);

        given()
            .queryParam("clienteId", clienteId)
        .when()
            .get("/simulacoes")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("[0].clienteId", equalTo(999));
    }

    // Cliente sem nenhuma simulação deve retornar lista vazia, não erro
    @Test
    void deveRetornarListaVaziaParaClienteSemHistorico() {
        given()
            .queryParam("clienteId", 99999)
        .when()
            .get("/simulacoes")
        .then()
            .statusCode(200)
            .body("size()", equalTo(0));
    }

    // CDB Caixa 2026: 12% a.a., R$ 10.000,00, 12 meses → valorFinal = 10000 × (1,01)^12 = R$ 11.268,25
    @Test
    void deveCalcularValorFinalCorreto() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 777,
                    "valor": 10000.00,
                    "prazoMeses": 12,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(201)
            .body("resultadoSimulacao.valorFinal", equalTo(11268.25f));
    }

    // Valor e prazo exatamente nos limites do produto devem ser aceitos (boundary inclusive)
    @Test
    void deveAceitarSimulacaoNosLimitesDoRangeDoProduto() {
        // Valor mínimo e prazo mínimo do CDB Caixa 2026 (R$ 1.000, 6 meses)
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 555,
                    "valor": 1000.00,
                    "prazoMeses": 6,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(201);

        // Valor máximo e prazo máximo do CDB Caixa 2026 (R$ 100.000, 24 meses)
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 555,
                    "valor": 100000.00,
                    "prazoMeses": 24,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(201);
    }

    // Valor fora do range do produto deve retornar 422
    @Test
    void deveRetornar422QuandoValorForaDoRangeDoProduto() {
        // Abaixo do mínimo do CDB Caixa 2026 (mínimo R$ 1.000,00)
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 666,
                    "valor": 999.99,
                    "prazoMeses": 12,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(422)
            .body("erro", notNullValue());

        // Acima do máximo de todos os produtos CDB (CDB Poupança Plus tem max R$ 500.000,00)
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "clienteId": 666,
                    "valor": 500000.01,
                    "prazoMeses": 12,
                    "tipoProduto": "CDB"
                }
                """)
        .when()
            .post("/simulacoes")
        .then()
            .statusCode(422)
            .body("erro", notNullValue());
    }
}