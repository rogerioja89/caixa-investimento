package com.github.rogerioja89.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraJurosTest {

    private CalculadoraJuros calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraJuros();
    }

    // CDB Caixa 2026: 12% a.a. × R$10.000 × 12 meses → R$11.268,25
    @Test
    void deveCalcularJurosCompostosCorretamente() {
        BigDecimal resultado = calculadora.calcularValorFinal(
            new BigDecimal("10000.00"),
            new BigDecimal("0.12"),
            12
        );
        assertEquals(new BigDecimal("11268.25"), resultado);
    }

    @Test
    void deveRetornarValorIgualParaPrazoZero() {
        BigDecimal resultado = calculadora.calcularValorFinal(
            new BigDecimal("5000.00"),
            new BigDecimal("0.12"),
            0
        );
        assertEquals(new BigDecimal("5000.00"), resultado);
    }

    @Test
    void deveAplicarArredondamentoHalfUp() {
        // LCI Caixa Agrícola: 10% a.a., R$5.000, 12 meses → R$5.523,57
        BigDecimal resultado = calculadora.calcularValorFinal(
            new BigDecimal("5000.00"),
            new BigDecimal("0.10"),
            12
        );
        assertEquals(new BigDecimal("5523.57"), resultado);
    }

    @Test
    void deveCalcularComPrazoLongo() {
        // CDB Poupança Plus: 14% a.a., R$10.000, 60 meses
        BigDecimal resultado = calculadora.calcularValorFinal(
            new BigDecimal("10000.00"),
            new BigDecimal("0.14"),
            60
        );
        assertNotNull(resultado);
        assertTrue(resultado.compareTo(new BigDecimal("10000.00")) > 0);
    }
}
