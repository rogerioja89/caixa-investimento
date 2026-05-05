package com.github.rogerioja89.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "simulacoes")
public class Simulacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "produto_nome", nullable = false)
    private String produtoNome;

    @Column(name = "tipo_produto", nullable = false)
    private String tipoProduto;

    @Column(name = "valor_investido", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorInvestido;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    @Column(name = "rentabilidade_aplicada", nullable = false, precision = 10, scale = 4)
    private BigDecimal rentabilidadeAplicada;

    @Column(name = "valor_final", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "data_simulacao", nullable = false)
    private LocalDateTime dataSimulacao;

    public Simulacao(Long clienteId, String produtoNome, String tipoProduto, BigDecimal valorInvestido,
                     Integer prazoMeses, BigDecimal rentabilidadeAplicada, BigDecimal valorFinal,
                     LocalDateTime dataSimulacao) {
        this.clienteId = clienteId;
        this.produtoNome = produtoNome;
        this.tipoProduto = tipoProduto;
        this.valorInvestido = valorInvestido;
        this.prazoMeses = prazoMeses;
        this.rentabilidadeAplicada = rentabilidadeAplicada;
        this.valorFinal = valorFinal;
        this.dataSimulacao = dataSimulacao;
    }
}