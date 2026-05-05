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

// Camada de negócio: aqui ficam as regras da aplicação.
// O Resource (controller) chama o Service, que por sua vez usa os Repositories.
// @Transactional garante que todas as operações de banco dentro do método
// sejam tratadas como uma única transação (tudo salva ou tudo reverte).
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
        // 1. Busca um produto elegível no banco
        Produto produto = produtoRepository
            .findElegivel(request.tipoProduto, request.valor, request.prazoMeses)
            .orElseThrow(() -> new WebApplicationException(
                Response.status(422)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"erro\": \"Nenhum produto elegível encontrado para os parâmetros informados\"}")
                        .build()
            ));

        // 2. Calcula o valor final: valorFinal = valor * (1 + rentabilidadeAnual/12) ^ prazoMeses
        BigDecimal valorFinal = calcularValorFinal(
            request.valor, produto.rentabilidadeAnual, request.prazoMeses
        );

        // 3. Cria e persiste o registro da simulação
        Simulacao simulacao = new Simulacao();
        simulacao.clienteId = request.clienteId;
        simulacao.produtoNome = produto.nome;
        simulacao.tipoProduto = produto.tipoProduto;
        simulacao.valorInvestido = request.valor;
        simulacao.prazoMeses = request.prazoMeses;
        simulacao.rentabilidadeAplicada = produto.rentabilidadeAnual;
        simulacao.valorFinal = valorFinal;
        simulacao.dataSimulacao = LocalDateTime.now();

        simulacaoRepository.persist(simulacao);

        // 4. Monta e retorna o DTO de resposta
        return simulacaoMapper.toResponseDTO(simulacao, produto);
    }

    public List<SimulacaoHistoricoDTO> buscarHistorico(Long clienteId) {
        return simulacaoRepository.findByClienteId(clienteId)
            .stream()
            .map(simulacaoMapper::toHistoricoDTO)
            .toList();
    }

    // Fórmula de juros compostos mensais
    // taxaMensal = rentabilidadeAnual / 12
    // valorFinal = valor * (1 + taxaMensal) ^ prazoMeses
    private BigDecimal calcularValorFinal(BigDecimal valor, BigDecimal rentabilidadeAnual, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal fator = BigDecimal.ONE.add(taxaMensal).pow(prazoMeses, MathContext.DECIMAL128);
        return valor.multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }
}
