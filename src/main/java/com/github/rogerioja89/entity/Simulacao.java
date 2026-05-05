package com.github.rogerioja89.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Simulacao() {
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getTipoProduto() { return tipoProduto; }
    public void setTipoProduto(String tipoProduto) { this.tipoProduto = tipoProduto; }

    public BigDecimal getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(BigDecimal valorInvestido) { this.valorInvestido = valorInvestido; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }

    public BigDecimal getRentabilidadeAplicada() { return rentabilidadeAplicada; }
    public void setRentabilidadeAplicada(BigDecimal rentabilidadeAplicada) { this.rentabilidadeAplicada = rentabilidadeAplicada; }

    public BigDecimal getValorFinal() { return valorFinal; }
    public void setValorFinal(BigDecimal valorFinal) { this.valorFinal = valorFinal; }

    public LocalDateTime getDataSimulacao() { return dataSimulacao; }
    public void setDataSimulacao(LocalDateTime dataSimulacao) { this.dataSimulacao = dataSimulacao; }
}
