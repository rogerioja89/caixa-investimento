package com.github.rogerioja89.dto;

import java.time.LocalDateTime;

public class SimulacaoResponseDTO {

    private ProdutoResponseDTO produtoValidado;
    private ResultadoSimulacaoDTO resultadoSimulacao;
    private LocalDateTime dataSimulacao;

    public SimulacaoResponseDTO() {
    }

    public SimulacaoResponseDTO(ProdutoResponseDTO produtoValidado, ResultadoSimulacaoDTO resultadoSimulacao,
                                 LocalDateTime dataSimulacao) {
        this.produtoValidado = produtoValidado;
        this.resultadoSimulacao = resultadoSimulacao;
        this.dataSimulacao = dataSimulacao;
    }

    public ProdutoResponseDTO getProdutoValidado() { return produtoValidado; }
    public void setProdutoValidado(ProdutoResponseDTO produtoValidado) { this.produtoValidado = produtoValidado; }

    public ResultadoSimulacaoDTO getResultadoSimulacao() { return resultadoSimulacao; }
    public void setResultadoSimulacao(ResultadoSimulacaoDTO resultadoSimulacao) { this.resultadoSimulacao = resultadoSimulacao; }

    public LocalDateTime getDataSimulacao() { return dataSimulacao; }
    public void setDataSimulacao(LocalDateTime dataSimulacao) { this.dataSimulacao = dataSimulacao; }
}