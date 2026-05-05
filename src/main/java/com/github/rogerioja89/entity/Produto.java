package com.github.rogerioja89.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "tipo_produto", nullable = false)
    private String tipoProduto;

    @Column(name = "rentabilidade_anual", nullable = false, precision = 10, scale = 4)
    private BigDecimal rentabilidadeAnual;

    @Column(nullable = false)
    private String risco;

    @Column(name = "prazo_min_meses", nullable = false)
    private Integer prazoMinMeses;

    @Column(name = "prazo_max_meses", nullable = false)
    private Integer prazoMaxMeses;

    @Column(name = "valor_min", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorMin;

    @Column(name = "valor_max", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorMax;

    public Produto() {
    }

    public Produto(String nome, String tipoProduto, BigDecimal rentabilidadeAnual, String risco,
                   Integer prazoMinMeses, Integer prazoMaxMeses, BigDecimal valorMin, BigDecimal valorMax) {
        this.nome = nome;
        this.tipoProduto = tipoProduto;
        this.rentabilidadeAnual = rentabilidadeAnual;
        this.risco = risco;
        this.prazoMinMeses = prazoMinMeses;
        this.prazoMaxMeses = prazoMaxMeses;
        this.valorMin = valorMin;
        this.valorMax = valorMax;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipoProduto() { return tipoProduto; }
    public void setTipoProduto(String tipoProduto) { this.tipoProduto = tipoProduto; }

    public BigDecimal getRentabilidadeAnual() { return rentabilidadeAnual; }
    public void setRentabilidadeAnual(BigDecimal rentabilidadeAnual) { this.rentabilidadeAnual = rentabilidadeAnual; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public Integer getPrazoMinMeses() { return prazoMinMeses; }
    public void setPrazoMinMeses(Integer prazoMinMeses) { this.prazoMinMeses = prazoMinMeses; }

    public Integer getPrazoMaxMeses() { return prazoMaxMeses; }
    public void setPrazoMaxMeses(Integer prazoMaxMeses) { this.prazoMaxMeses = prazoMaxMeses; }

    public BigDecimal getValorMin() { return valorMin; }
    public void setValorMin(BigDecimal valorMin) { this.valorMin = valorMin; }

    public BigDecimal getValorMax() { return valorMax; }
    public void setValorMax(BigDecimal valorMax) { this.valorMax = valorMax; }
}