package com.github.rogerioja89.dto;

import java.math.BigDecimal;

public class ResultadoSimulacaoDTO {

    private BigDecimal valorFinal;
    private Integer prazoMeses;

    public ResultadoSimulacaoDTO() {
    }

    public ResultadoSimulacaoDTO(BigDecimal valorFinal, Integer prazoMeses) {
        this.valorFinal = valorFinal;
        this.prazoMeses = prazoMeses;
    }

    public BigDecimal getValorFinal() { return valorFinal; }
    public void setValorFinal(BigDecimal valorFinal) { this.valorFinal = valorFinal; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }
}
