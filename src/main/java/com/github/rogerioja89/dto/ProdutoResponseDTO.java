package com.github.rogerioja89.dto;

import java.math.BigDecimal;

public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String tipo;
    private BigDecimal rentabilidade;
    private String risco;

    public ProdutoResponseDTO() {
    }

    public ProdutoResponseDTO(Long id, String nome, String tipo, BigDecimal rentabilidade, String risco) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.rentabilidade = rentabilidade;
        this.risco = risco;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getRentabilidade() { return rentabilidade; }
    public void setRentabilidade(BigDecimal rentabilidade) { this.rentabilidade = rentabilidade; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }
}
