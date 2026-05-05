package com.github.rogerioja89.mapper;

import com.github.rogerioja89.dto.ProdutoResponseDTO;
import com.github.rogerioja89.entity.Produto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoMapper {

    public ProdutoResponseDTO toDTO(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getTipoProduto(),
            produto.getRentabilidadeAnual(),
            produto.getRisco()
        );
    }
}