package com.senac.ApiAvRestaurante.application.dto.restaurante;

import com.senac.ApiAvRestaurante.domain.entities.Restaurante;

public record RestauranteResponseDto(Long id, String nome) {

    public RestauranteResponseDto(Restaurante restaurante){
        this(
                restaurante.getId(),
                restaurante.getNome()

        );
    }
}
