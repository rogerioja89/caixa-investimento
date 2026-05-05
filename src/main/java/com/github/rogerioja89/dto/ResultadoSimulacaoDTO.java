package com.github.rogerioja89.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoSimulacaoDTO {

    private BigDecimal valorFinal;
    private Integer prazoMeses;
}