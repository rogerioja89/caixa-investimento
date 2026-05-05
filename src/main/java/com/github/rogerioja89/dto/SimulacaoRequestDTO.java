package com.github.rogerioja89.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

// DTO (Data Transfer Object) que representa o JSON de entrada do POST /simulacoes.
// As anotações @NotNull, @Positive e @NotBlank são do Hibernate Validator e
// serão checadas automaticamente pelo Quarkus antes de entrar no Resource.
public class SimulacaoRequestDTO {

    @NotNull(message = "clienteId é obrigatório")
    @Positive(message = "clienteId deve ser um número positivo")
    public Long clienteId;

    @NotNull(message = "valor é obrigatório")
    @Positive(message = "valor deve ser positivo")
    public BigDecimal valor;

    @NotNull(message = "prazoMeses é obrigatório")
    @Positive(message = "prazoMeses deve ser positivo")
    public Integer prazoMeses;

    @NotBlank(message = "tipoProduto é obrigatório")
    public String tipoProduto;
}