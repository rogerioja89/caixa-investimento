package com.github.rogerioja89.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SimulacaoRequestDTO {

    @NotNull(message = "clienteId é obrigatório")
    @Positive(message = "clienteId deve ser um número positivo")
    private Long clienteId;

    @NotNull(message = "valor é obrigatório")
    @Positive(message = "valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "prazoMeses é obrigatório")
    @Positive(message = "prazoMeses deve ser positivo")
    private Integer prazoMeses;

    @NotBlank(message = "tipoProduto é obrigatório")
    private String tipoProduto;
}