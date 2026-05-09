package com.github.rogerioja89.mapper;

import com.github.rogerioja89.dto.ProdutoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoMapperTest {

    private ProdutoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProdutoMapper();
    }

    @Test
    void deveMappearProdutoParaDTO() {
        Produto produto = new Produto(
            "CDB Caixa 2026", "CDB", new BigDecimal("0.12"), "Baixo",
            6, 24, new BigDecimal("1000.00"), new BigDecimal("100000.00")
        );
        produto.setId(1L);

        ProdutoResponseDTO dto = mapper.toDTO(produto);

        assertEquals(1L, dto.getId());
        assertEquals("CDB Caixa 2026", dto.getNome());
        assertEquals("CDB", dto.getTipo());
        assertEquals(new BigDecimal("0.12"), dto.getRentabilidade());
        assertEquals("Baixo", dto.getRisco());
    }

    @Test
    void deveMappearTodosOsCamposCorretamente() {
        Produto produto = new Produto(
            "LCI Premium", "LCI", new BigDecimal("0.13"), "Alto",
            24, 48, new BigDecimal("50000.00"), new BigDecimal("1000000.00")
        );
        produto.setId(5L);

        ProdutoResponseDTO dto = mapper.toDTO(produto);

        assertAll(
            () -> assertEquals(5L, dto.getId()),
            () -> assertEquals("LCI Premium", dto.getNome()),
            () -> assertEquals("LCI", dto.getTipo()),
            () -> assertEquals(new BigDecimal("0.13"), dto.getRentabilidade()),
            () -> assertEquals("Alto", dto.getRisco())
        );
    }
}
