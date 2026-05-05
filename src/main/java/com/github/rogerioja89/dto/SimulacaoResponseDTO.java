package com.github.rogerioja89.dto;

import java.time.LocalDateTime;

// DTO de saída do POST /simulacoes. Agrupa o produto validado, o resultado e a data.
public class SimulacaoResponseDTO {

    public ProdutoResponseDTO produtoValidado;
    public ResultadoSimulacaoDTO resultadoSimulacao;
    public LocalDateTime dataSimulacao;
}