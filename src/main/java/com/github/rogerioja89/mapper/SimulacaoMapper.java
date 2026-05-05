package com.github.rogerioja89.mapper;

import com.github.rogerioja89.dto.ResultadoSimulacaoDTO;
import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.entity.Simulacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

// Converte a entidade Simulacao em DTOs de resposta.
// @Inject faz o Quarkus injetar automaticamente o ProdutoMapper (sem precisar fazer "new").
@ApplicationScoped
public class SimulacaoMapper {

    @Inject
    ProdutoMapper produtoMapper;

    // Monta o DTO completo de resposta do POST /simulacoes
    public SimulacaoResponseDTO toResponseDTO(Simulacao simulacao, Produto produto) {
        SimulacaoResponseDTO dto = new SimulacaoResponseDTO();
        dto.produtoValidado = produtoMapper.toDTO(produto);
        dto.resultadoSimulacao = new ResultadoSimulacaoDTO();
        dto.resultadoSimulacao.valorFinal = simulacao.valorFinal;
        dto.resultadoSimulacao.prazoMeses = simulacao.prazoMeses;
        dto.dataSimulacao = simulacao.dataSimulacao;
        return dto;
    }

    // Monta o DTO de um item do histórico para o GET /simulacoes
    public SimulacaoHistoricoDTO toHistoricoDTO(Simulacao simulacao) {
        SimulacaoHistoricoDTO dto = new SimulacaoHistoricoDTO();
        dto.id = simulacao.id;
        dto.clienteId = simulacao.clienteId;
        dto.produto = simulacao.produtoNome;
        dto.valorInvestido = simulacao.valorInvestido;
        dto.valorFinal = simulacao.valorFinal;
        dto.prazoMeses = simulacao.prazoMeses;
        dto.dataSimulacao = simulacao.dataSimulacao;
        return dto;
    }
}