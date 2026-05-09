package com.github.rogerioja89;

import com.github.rogerioja89.entity.Produto;
import com.github.rogerioja89.repository.ProdutoRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class DataSeeder {

    @Inject
    ProdutoRepository produtoRepository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        // Idempotente: o banco SQLite persiste entre restarts; só popula se ainda estiver vazio.
        if (produtoRepository.count() > 0) {
            return;
        }

        produtoRepository.persist(produto("CDB Caixa 2026",     "CDB", "0.1200", "Baixo",  6,  24,  1000.00,  100000.00));
        produtoRepository.persist(produto("LCI Caixa Agrícola", "LCI", "0.1000", "Baixo", 12,  36,  5000.00,  200000.00));
        produtoRepository.persist(produto("LCA Sustentável",    "LCA", "0.1100", "Médio",  6,  12,  2000.00,  150000.00));
        produtoRepository.persist(produto("CDB Poupança Plus",  "CDB", "0.1400", "Médio", 12,  60, 10000.00,  500000.00));
        produtoRepository.persist(produto("LCI Premium",        "LCI", "0.1300", "Alto",  24,  48, 50000.00, 1000000.00));
    }

    private Produto produto(String nome, String tipo, String rentabilidade,
                            String risco, int prazoMin, int prazoMax,
                            double valorMin, double valorMax) {
        return new Produto(
            nome,
            tipo,
            new BigDecimal(rentabilidade),
            risco,
            prazoMin,
            prazoMax,
            BigDecimal.valueOf(valorMin),
            BigDecimal.valueOf(valorMax)
        );
    }
}