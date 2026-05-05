package com.github.rogerioja89.service;

import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoRequestDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.entity.Simulacao;
import com.github.rogerioja89.mapper.SimulacaoMapper;
import com.github.rogerioja89.repository.ProdutoRepository;
import com.github.rogerioja89.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    SimulacaoRepository simulacaoRepository;

    @Inject
    SimulacaoMapper simulacaoMapper;

    @Transactional
    public SimulacaoResponseDTO simular(SimulacaoRequestDTO request) {
        Produto produto = produtoRepository
            .findElegivel(request.getTipoProduto(), request.getValor(), request.getPrazoMeses())
            .orElseThrow(() -> new WebApplicationException(
                Response.status(422)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"erro\": \"Nenhum produto elegível encontrado para os parâmetros informados\"}")
                        .build()
            ));

        BigDecimal valorFinal = calcularValorFinal(
            request.getValor(), produto.getRentabilidadeAnual(), request.getPrazoMeses()
        );

        Simulacao simulacao = new Simulacao(
            request.getClienteId(),
            produto.getNome(),
            produto.getTipoProduto(),
            request.getValor(),
            request.getPrazoMeses(),
            produto.getRentabilidadeAnual(),
            valorFinal,
            LocalDateTime.now()
        );

        simulacaoRepository.persist(simulacao);

        return simulacaoMapper.toResponseDTO(simulacao, produto);
    }

    public List<SimulacaoHistoricoDTO> buscarHistorico(Long clienteId) {
        return simulacaoRepository.findByClienteId(clienteId)
            .stream()
            .map(simulacaoMapper::toHistoricoDTO)
            .toList();
    }

    private BigDecimal calcularValorFinal(BigDecimal valor, BigDecimal rentabilidadeAnual, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal fator = BigDecimal.ONE.add(taxaMensal).pow(prazoMeses, MathContext.DECIMAL128);
        return valor.multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }
}