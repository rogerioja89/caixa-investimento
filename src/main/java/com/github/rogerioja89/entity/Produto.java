package com.github.rogerioja89.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
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
}