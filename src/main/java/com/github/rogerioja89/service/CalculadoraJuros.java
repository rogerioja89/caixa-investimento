package com.github.rogerioja89.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@ApplicationScoped
public class CalculadoraJuros {

    // Juros compostos mensais: valorFinal = valor × (1 + rentabilidadeAnual/12) ^ prazoMeses
    public BigDecimal calcularValorFinal(BigDecimal valor, BigDecimal rentabilidadeAnual, int prazoMeses) {
        BigDecimal taxaMensal = rentabilidadeAnual.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        // DECIMAL128 mantém precisão suficiente durante a exponenciação com BigDecimal.
        BigDecimal fator = BigDecimal.ONE.add(taxaMensal).pow(prazoMeses, MathContext.DECIMAL128);
        return valor.multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }
}
