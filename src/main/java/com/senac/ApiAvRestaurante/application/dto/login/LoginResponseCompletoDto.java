package com.senac.ApiAvRestaurante.application.dto.login;

import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioResponseDto;

public record LoginResponseCompletoDto(
        String token,
        UsuarioResponseDto usuario,
        String tipoPerfil
) {
}
