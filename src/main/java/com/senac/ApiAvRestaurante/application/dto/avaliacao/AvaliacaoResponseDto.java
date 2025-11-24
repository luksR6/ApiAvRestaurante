package com.senac.ApiAvRestaurante.application.dto.avaliacao;

import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioResponseDto;

public record AvaliacaoResponseDto(
        Long id,
        int nota,
        String comentario,
        String nomeRestaurante,
        Double mediaNotaDoRestaurante,
        UsuarioResponseDto usuario
) {
}