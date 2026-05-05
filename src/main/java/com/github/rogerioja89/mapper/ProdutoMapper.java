package com.github.rogerioja89.mapper;

import com.github.rogerioja89.dto.ProdutoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import jakarta.enterprise.context.ApplicationScoped;

// Responsável apenas por converter a entidade Produto em ProdutoResponseDTO.
// @ApplicationScoped significa que o Quarkus cria uma única instância reutilizável (Singleton no CDI).
@ApplicationScoped
public class ProdutoMapper {

    public ProdutoResponseDTO toDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.id = produto.id;
        dto.nome = produto.nome;
        dto.tipo = produto.tipoProduto;
        dto.rentabilidade = produto.rentabilidadeAnual;
        dto.risco = produto.risco;
        return dto;
    }
}