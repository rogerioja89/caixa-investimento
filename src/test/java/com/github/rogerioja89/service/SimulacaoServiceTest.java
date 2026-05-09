package com.github.rogerioja89.service;

import com.github.rogerioja89.dto.SimulacaoRequestDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.entity.Simulacao;
import com.github.rogerioja89.exception.NegocioException;
import com.github.rogerioja89.mapper.ProdutoMapper;
import com.github.rogerioja89.mapper.SimulacaoMapper;
import com.github.rogerioja89.repository.ProdutoRepository;
import com.github.rogerioja89.repository.SimulacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulacaoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private SimulacaoRepository simulacaoRepository;

    @Mock
    private SimulacaoMapper simulacaoMapper;

    @Mock
    private CalculadoraJuros calculadoraJuros;

    @InjectMocks
    private SimulacaoService simulacaoService;

    private Produto produtoMock;
    private SimulacaoRequestDTO requestMock;

    @BeforeEach
    void setUp() {
        produtoMock = new Produto(
            "CDB Caixa 2026", "CDB", new BigDecimal("0.12"), "Baixo",
            6, 24, new BigDecimal("1000.00"), new BigDecimal("100000.00")
        );
        produtoMock.setId(1L);

        requestMock = new SimulacaoRequestDTO();
        requestMock.setClienteId(123L);
        requestMock.setValor(new BigDecimal("10000.00"));
        requestMock.setPrazoMeses(12);
        requestMock.setTipoProduto("CDB");
    }

    @Test
    void deveRetornar422QuandoNenhumProdutoElegivel() {
        when(produtoRepository.findElegivel(anyString(), any(BigDecimal.class), anyInt()))
            .thenReturn(Optional.empty());

        NegocioException ex = assertThrows(NegocioException.class,
            () -> simulacaoService.simular(requestMock));

        assertEquals(422, ex.getStatus());
        assertTrue(ex.getMessage().contains("Nenhum produto elegível"));
    }

    @Test
    void deveChamarCalculadoraComOsParametrosCorretos() {
        BigDecimal valorFinal = new BigDecimal("11268.25");

        when(produtoRepository.findElegivel(anyString(), any(BigDecimal.class), anyInt()))
            .thenReturn(Optional.of(produtoMock));
        when(calculadoraJuros.calcularValorFinal(any(), any(), anyInt()))
            .thenReturn(valorFinal);

        Simulacao simulacaoMock = new Simulacao(
            123L, "CDB Caixa 2026", "CDB",
            new BigDecimal("10000.00"), 12, new BigDecimal("0.12"),
            valorFinal, LocalDateTime.now()
        );
        SimulacaoResponseDTO responseMock = mock(SimulacaoResponseDTO.class);
        when(simulacaoMapper.toResponseDTO(any(Simulacao.class), any(Produto.class)))
            .thenReturn(responseMock);

        simulacaoService.simular(requestMock);

        verify(calculadoraJuros).calcularValorFinal(
            new BigDecimal("10000.00"),
            new BigDecimal("0.12"),
            12
        );
    }

    @Test
    void devePersistirSimulacaoAposCalculo() {
        BigDecimal valorFinal = new BigDecimal("11268.25");

        when(produtoRepository.findElegivel(anyString(), any(BigDecimal.class), anyInt()))
            .thenReturn(Optional.of(produtoMock));
        when(calculadoraJuros.calcularValorFinal(any(), any(), anyInt()))
            .thenReturn(valorFinal);
        when(simulacaoMapper.toResponseDTO(any(Simulacao.class), any(Produto.class)))
            .thenReturn(mock(SimulacaoResponseDTO.class));

        simulacaoService.simular(requestMock);

        // Persist é chamado pelo Panache — verifica que o repository recebeu a chamada
        verify(simulacaoRepository).persist(any(Simulacao.class));
    }
}
