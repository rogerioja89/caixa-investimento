package com.github.rogerioja89.service;

import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoRequestDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.entity.Simulacao;
import com.github.rogerioja89.exception.NegocioException;
import com.github.rogerioja89.mapper.SimulacaoMapper;
import io.quarkus.logging.Log;
import com.github.rogerioja89.repository.ProdutoRepository;
import com.github.rogerioja89.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
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

    @Inject
    CalculadoraJuros calculadoraJuros;

    // @Transactional: leitura do produto e gravação da simulação devem ocorrer na mesma transação.
    @Transactional
    public SimulacaoResponseDTO simular(SimulacaoRequestDTO request) {
        Log.infof("Iniciando simulação: clienteId=%d, tipoProduto=%s, valor=%s, prazoMeses=%d",
            request.getClienteId(), request.getTipoProduto(),
            request.getValor(), request.getPrazoMeses());

        Produto produto = produtoRepository
            .findElegivel(request.getTipoProduto(), request.getValor(), request.getPrazoMeses())
            // 422 Unprocessable Entity: dados válidos, mas nenhum produto atende os critérios informados.
            .orElseThrow(() -> {
                Log.warnf("Nenhum produto elegível: tipoProduto=%s, valor=%s, prazoMeses=%d",
                    request.getTipoProduto(), request.getValor(), request.getPrazoMeses());
                return new NegocioException(422, "Nenhum produto elegível encontrado para os parâmetros informados");
            });

        Log.infof("Produto selecionado: %s", produto.getNome());

        BigDecimal valorFinal = calculadoraJuros.calcularValorFinal(
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

        Log.infof("Simulação concluída: id=%d, valorFinal=%s", simulacao.getId(), valorFinal);

        return simulacaoMapper.toResponseDTO(simulacao, produto);
    }

    public List<SimulacaoHistoricoDTO> buscarHistorico(Long clienteId) {
        Log.debugf("Buscando histórico: clienteId=%d", clienteId);
        return simulacaoRepository.findByClienteId(clienteId)
            .stream()
            .map(simulacaoMapper::toHistoricoDTO)
            .toList();
    }

}