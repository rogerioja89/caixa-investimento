package com.github.rogerioja89;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// @QuarkusTest sobe a aplicação completa em modo de teste.
// O banco H2 em memória é usado (configurado em application.properties com prefixo %test).
// O import.sql é executado automaticamente, populando os produtos.
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
            .statusCode(422);
    }

    // Campos inválidos devem retornar HTTP 400 (tratado pelo Hibernate Validator)
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
            .statusCode(400);
    }

    // clienteId ausente no GET deve retornar 400
    @Test
    void deveRetornar400QuandoClienteIdAusente() {
        given()
        .when()
            .get("/simulacoes")
        .then()
            .statusCode(400);
    }

    // Após criar uma simulação, o histórico deve conter o registro
    @Test
    void deveBuscarHistoricoDoCliente() {
        Long clienteId = 999L;

        // Cria uma simulação para o clienteId 999
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

        // Verifica que o histórico retorna pelo menos 1 item
        given()
            .queryParam("clienteId", clienteId)
        .when()
            .get("/simulacoes")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("[0].clienteId", equalTo(999));
    }
}