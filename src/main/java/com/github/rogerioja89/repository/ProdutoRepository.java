package com.github.rogerioja89.repository;

import com.github.rogerioja89.entity.Produto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Optional;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

    // JPQL usa nomes dos campos Java (camelCase), não os nomes das colunas do banco (snake_case).
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