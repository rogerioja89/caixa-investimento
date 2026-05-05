package com.github.rogerioja89.dto;

import java.math.BigDecimal;

// Representa os dados do produto validado que será retornado na resposta da simulação.
public class ProdutoResponseDTO {

    public Long id;
    public String nome;
    public String tipo;
    public BigDecimal rentabilidade;
    public String risco;
}