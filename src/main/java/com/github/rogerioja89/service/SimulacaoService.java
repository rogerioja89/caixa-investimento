package com.github.rogerioja89.service;

import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoRequestDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.entity.Simulacao;
import com.github.rogerioja89.exception.NegocioException;
import com.github.rogerioja89.mapper.SimulacaoMapper;
import com.github.rogerioja89.repository.ProdutoRepository;
import com.github.rogerioja89.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    // @Transactional: leitura do produto e gravação da simulação devem ocorrer na mesma transação.
    @Transactional
    public SimulacaoResponseDTO simular(SimulacaoRequestDTO request) {
        Produto produto = produtoRepository
            .findElegivel(request.getTipoProduto(), request.getValor(), request.getPrazoMeses())
            // 422 Unprocessable Entity: dados válidos, mas nenhum produto atende os critérios informados.
            .orElseThrow(() -> new NegocioException(422, "Nenhum produto elegível encontrado para os parâmetros informados"));

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

    // Juros compostos mensais: valorFinal = valor × (1 + rentabilidadeAnual/12) ^ prazoMeses
    private BigDecimal calcularValorFinal(BigDecimal valor, BigDecimal rentabilidadeAnual, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        // DECIMAL128 mantém precisão suficiente durante a exponenciação com BigDecimal.
        BigDecimal fator = BigDecimal.ONE.add(taxaMensal).pow(prazoMeses, MathContext.DECIMAL128);
        return valor.multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }
}