package com.github.rogerioja89.repository;

import com.github.rogerioja89.entity.Simulacao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

// Repositório responsável pelo acesso à tabela "simulacoes".
@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {

    // Retorna todas as simulações de um cliente específico, ordenadas da mais recente para a mais antiga.
    public List<Simulacao> findByClienteId(Long clienteId) {
        return list("clienteId = ?1 ORDER BY dataSimulacao DESC", clienteId);
    }
}