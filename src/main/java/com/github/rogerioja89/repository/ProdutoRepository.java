package com.github.rogerioja89.repository;

import com.github.rogerioja89.entity.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Optional;

// Repositório responsável pelo acesso à tabela "produtos".
// PanacheRepository já fornece métodos prontos: findById, list, persist, delete, count, etc.
@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

    // Busca o primeiro produto que seja elegível para a simulação:
    // - mesmo tipo (CDB, LCI, LCA...)
    // - valor investido dentro do range permitido (valorMin <= valor <= valorMax)
    // - prazo dentro do range permitido (prazoMinMeses <= prazo <= prazoMaxMeses)
    public Optional<Produto> findElegivel(String tipoProduto, BigDecimal valor, Integer prazoMeses) {
        return find(
            "tipoProduto = :tipo "
            + "AND valorMin <= :valor AND valorMax >= :valor "
            + "AND prazoMinMeses <= :prazo AND prazoMaxMeses >= :prazo",
            Parameters.with("tipo", tipoProduto)
                      .and("valor", valor)
                      .and("prazo", prazoMeses)
        ).firstResultOptional();
    }
}