package com.senac.ApiAvRestaurante.application.dto.avaliacao;

public record AvaliacaoResponseDto(
        Long id,
        int nota,
        String comentario,
        String nomeRestaurante,
        Double mediaNotaDoRestaurante
) {
}