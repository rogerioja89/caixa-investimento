# caixa-investimento

API REST de simulação de investimentos da CAIXA. Permite simular o valor final de um investimento com base em produtos parametrizados em banco de dados, calculando juros compostos e persistindo o histórico por cliente.

---

## Tecnologias

| Tecnologia | Versão | Finalidade |
|-----------|--------|-----------|
| Java | 21 | Linguagem |
| Quarkus | 3.35.2 | Framework |
| Hibernate ORM Panache | — | ORM + Repository pattern |
| Hibernate Validator | — | Validação de entrada (Bean Validation) |
| Lombok | 1.18.36 | Redução de boilerplate |
| SQLite | — | Banco de dados (desenvolvimento) |
| H2 | — | Banco de dados em memória (testes) |
| JBoss Logging | — | Logging nativo do Quarkus |
| REST Assured | — | Testes de integração da API |

---

## Estrutura do projeto

```
src/main/java/com/github/rogerioja89/
│
├── DataSeeder.java               ← Popula os produtos na inicialização (idempotente)
│
├── entity/
│   ├── Produto.java              ← Entidade JPA — tabela "produtos"
│   └── Simulacao.java            ← Entidade JPA — tabela "simulacoes"
│
├── dto/
│   ├── SimulacaoRequestDTO.java  ← Corpo do POST (com validações)
│   ├── SimulacaoResponseDTO.java ← Resposta do POST
│   ├── SimulacaoHistoricoDTO.java← Item do GET histórico
│   ├── ProdutoResponseDTO.java   ← Dados do produto na resposta
│   └── ResultadoSimulacaoDTO.java← Resultado do cálculo
│
├── exception/
│   ├── ErroResponse.java                    ← DTO padrão de erro: {"erro": "..."}
│   ├── NegocioException.java                ← Exceção de regra de negócio com status HTTP
│   ├── NegocioExceptionMapper.java          ← Mapeia NegocioException → JSON de erro
│   └── ConstraintViolationExceptionMapper.java ← Mapeia erros de validação → JSON de erro
│
├── mapper/
│   ├── ProdutoMapper.java        ← Produto → ProdutoResponseDTO
│   └── SimulacaoMapper.java      ← Simulacao → DTOs de resposta
│
├── repository/
│   ├── ProdutoRepository.java    ← Query de elegibilidade
│   └── SimulacaoRepository.java  ← Busca histórico por clienteId
│
├── service/
│   └── SimulacaoService.java     ← Regras de negócio, cálculo e logging
│
└── resource/
    └── SimulacaoResource.java    ← Endpoints REST

src/test/java/com/github/rogerioja89/
└── SimulacaoResourceTest.java    ← 9 testes de integração com H2
```

---

## Como executar

### Pré-requisitos

- Java 21+
- Maven 3.9+

### Modo desenvolvimento

```bash
./mvnw quarkus:dev
```

A API sobe em `http://localhost:8080`. O banco SQLite (`investimentos.db`) é criado automaticamente na raiz do projeto na primeira execução.

### Rodar os testes

```bash
./mvnw test
```

Os testes usam H2 em memória — o banco é zerado e repopulado a cada execução.

---

## Produtos disponíveis

Inseridos automaticamente pelo `DataSeeder` na primeira inicialização:

| Nome | Tipo | Rentabilidade Anual | Risco | Prazo (meses) | Valor mínimo (R$) | Valor máximo (R$) |
|------|------|--------------------:|-------|:---:|---:|---:|
| CDB Caixa 2026 | CDB | 12% | Baixo | 6 a 24 | 1.000 | 100.000 |
| LCI Caixa Agrícola | LCI | 10% | Baixo | 12 a 36 | 5.000 | 200.000 |
| LCA Sustentável | LCA | 11% | Médio | 6 a 12 | 2.000 | 150.000 |
| CDB Poupança Plus | CDB | 14% | Médio | 12 a 60 | 10.000 | 500.000 |
| LCI Premium | LCI | 13% | Alto | 24 a 48 | 50.000 | 1.000.000 |

---

## Endpoints

### POST /simulacoes

Realiza uma simulação de investimento e persiste o resultado no histórico do cliente.

**Request**
```http
POST http://localhost:8080/simulacoes
Content-Type: application/json

{
  "clienteId": 123,
  "valor": 10000.00,
  "prazoMeses": 12,
  "tipoProduto": "CDB"
}
```

| Campo | Tipo | Obrigatório | Regra |
|-------|------|:-----------:|-------|
| `clienteId` | Long | Sim | Número positivo |
| `valor` | BigDecimal | Sim | Valor positivo |
| `prazoMeses` | Integer | Sim | Número positivo |
| `tipoProduto` | String | Sim | Não pode ser vazio |

**Response 201 — Criado com sucesso**
```json
{
  "produtoValidado": {
    "id": 1,
    "nome": "CDB Caixa 2026",
    "tipo": "CDB",
    "rentabilidade": 0.1200,
    "risco": "Baixo"
  },
  "resultadoSimulacao": {
    "valorFinal": 11268.25,
    "prazoMeses": 12
  },
  "dataSimulacao": "2026-05-09T10:00:00"
}
```

**Response 400 — Campos inválidos**
```json
{
  "erro": "valor: valor deve ser positivo; tipoProduto: tipoProduto é obrigatório"
}
```

**Response 422 — Nenhum produto elegível**
```json
{
  "erro": "Nenhum produto elegível encontrado para os parâmetros informados"
}
```

---

### GET /simulacoes?clienteId={id}

Retorna o histórico de simulações de um cliente, ordenado da mais recente para a mais antiga.

**Request**
```http
GET http://localhost:8080/simulacoes?clienteId=123
```

**Response 200 — Histórico encontrado**
```json
[
  {
    "id": 1,
    "clienteId": 123,
    "produto": "CDB Caixa 2026",
    "valorInvestido": 10000.00,
    "valorFinal": 11268.25,
    "prazoMeses": 12,
    "dataSimulacao": "2026-05-09T10:00:00"
  }
]
```

**Response 200 — Cliente sem histórico**
```json
[]
```

**Response 400 — Parâmetro ausente**
```json
{
  "erro": "O parâmetro clienteId é obrigatório"
}
```

---

## Exemplos prontos para executar

Salve o conteúdo abaixo em um arquivo `.http` e execute diretamente no IntelliJ IDEA ou no VS Code com a extensão [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

```http
### Simulação válida — CDB
POST http://localhost:8080/simulacoes
Content-Type: application/json

{
  "clienteId": 123,
  "valor": 10000.00,
  "prazoMeses": 12,
  "tipoProduto": "CDB"
}

###

### Simulação válida — LCI
POST http://localhost:8080/simulacoes
Content-Type: application/json

{
  "clienteId": 123,
  "valor": 10000.00,
  "prazoMeses": 12,
  "tipoProduto": "LCI"
}

###

### Erro 422 — Tipo de produto inexistente
POST http://localhost:8080/simulacoes
Content-Type: application/json

{
  "clienteId": 123,
  "valor": 10000.00,
  "prazoMeses": 12,
  "tipoProduto": "DEBENTURE"
}

###

### Erro 400 — Campos inválidos
POST http://localhost:8080/simulacoes
Content-Type: application/json

{
  "clienteId": -1,
  "valor": -500,
  "prazoMeses": 0,
  "tipoProduto": ""
}

###

### Histórico do cliente
GET http://localhost:8080/simulacoes?clienteId=123

###

### Erro 400 — clienteId ausente
GET http://localhost:8080/simulacoes
```

---

## Fórmula de cálculo

Juros compostos mensais com `BigDecimal` para precisão financeira:

```
valorFinal = valor × (1 + rentabilidadeAnual / 12) ^ prazoMeses
```

Exemplo: R$ 10.000 a 12% a.a. por 12 meses = **R$ 11.268,25**

---

## Formato de erros

Todos os erros retornam o mesmo formato JSON:

```json
{ "erro": "Descrição do problema" }
```

| Status | Situação |
|--------|----------|
| `400` | Campo inválido ou ausente. Para violações de Bean Validation, o campo `erro` lista todas: `"campo: mensagem; campo: mensagem"` |
| `422` | Dados válidos, mas nenhum produto elegível para os critérios informados |

---

## Testes

9 testes de integração em `SimulacaoResourceTest.java`:

| Teste | O que valida |
|-------|-------------|
| `deveCriarSimulacaoComSucesso` | POST válido retorna 201 com estrutura correta |
| `deveCalcularValorFinalCorreto` | Valor exato: R$ 10.000 × (1,01)^12 = R$ 11.268,25 |
| `deveRetornar422QuandoNaoHaProdutoElegivel` | Tipo inexistente retorna 422 com campo `erro` |
| `deveRetornar400QuandoCamposInvalidos` | Campos negativos/vazios retornam 400 com campo `erro` |
| `deveRetornar400QuandoClienteIdAusente` | GET sem clienteId retorna 400 com mensagem exata |
| `deveBuscarHistoricoDoCliente` | Cria simulação e confirma no histórico |
| `deveRetornarListaVaziaParaClienteSemHistorico` | Cliente sem histórico retorna 200 com `[]` |
| `deveAceitarSimulacaoNosLimitesDoRangeDoProduto` | Valor e prazo exatamente nos limites são aceitos |
| `deveRetornar422QuandoValorForaDoRangeDoProduto` | Abaixo do mínimo e acima do máximo retornam 422 |

---

## Banco de dados

| Perfil | Banco | Arquivo | Estratégia |
|--------|-------|---------|-----------|
| Desenvolvimento | SQLite | `investimentos.db` (raiz do projeto) | `update` — preserva dados entre restarts |
| Testes | H2 em memória | — | `drop-and-create` — zerado a cada execução |
