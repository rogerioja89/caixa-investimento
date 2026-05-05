package com.github.rogerioja89.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

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

    public SimulacaoRequestDTO() {
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }

    public String getTipoProduto() { return tipoProduto; }
    public void setTipoProduto(String tipoProduto) { this.tipoProduto = tipoProduto; }
}
