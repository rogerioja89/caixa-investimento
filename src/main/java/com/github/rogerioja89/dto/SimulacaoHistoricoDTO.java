package com.github.rogerioja89.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO de saída do GET /simulacoes. Representa um item do histórico de simulações do cliente.
public class SimulacaoHistoricoDTO {

    public Long id;
    public Long clienteId;
    public String produto;
    public BigDecimal valorInvestido;
    public BigDecimal valorFinal;
    public Integer prazoMeses;
    public LocalDateTime dataSimulacao;
}