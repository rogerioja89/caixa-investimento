package com.github.rogerioja89.mapper;

import com.github.rogerioja89.dto.ResultadoSimulacaoDTO;
import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.entity.Simulacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SimulacaoMapper {

    @Inject
    ProdutoMapper produtoMapper;

    public SimulacaoResponseDTO toResponseDTO(Simulacao simulacao, Produto produto) {
        ResultadoSimulacaoDTO resultado = new ResultadoSimulacaoDTO(
            simulacao.getValorFinal(),
            simulacao.getPrazoMeses()
        );
        return new SimulacaoResponseDTO(
            produtoMapper.toDTO(produto),
            resultado,
            simulacao.getDataSimulacao()
        );
    }

    public SimulacaoHistoricoDTO toHistoricoDTO(Simulacao simulacao) {
        return new SimulacaoHistoricoDTO(
            simulacao.getId(),
            simulacao.getClienteId(),
            simulacao.getProdutoNome(),
            simulacao.getValorInvestido(),
            simulacao.getValorFinal(),
            simulacao.getPrazoMeses(),
            simulacao.getDataSimulacao()
        );
    }
}