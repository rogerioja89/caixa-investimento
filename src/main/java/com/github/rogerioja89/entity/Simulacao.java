package com.github.rogerioja89.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Representa o histórico de cada simulação realizada por um cliente.
@Entity
@Table(name = "simulacoes")
public class Simulacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "cliente_id", nullable = false)
    public Long clienteId;

    @Column(name = "produto_nome", nullable = false)
    public String produtoNome;

    @Column(name = "tipo_produto", nullable = false)
    public String tipoProduto;

    @Column(name = "valor_investido", nullable = false, precision = 15, scale = 2)
    public BigDecimal valorInvestido;

    @Column(name = "prazo_meses", nullable = false)
    public Integer prazoMeses;

    @Column(name = "rentabilidade_aplicada", nullable = false, precision = 10, scale = 4)
    public BigDecimal rentabilidadeAplicada;

    @Column(name = "valor_final", nullable = false, precision = 15, scale = 2)
    public BigDecimal valorFinal;

    @Column(name = "data_simulacao", nullable = false)
    public LocalDateTime dataSimulacao;
}
