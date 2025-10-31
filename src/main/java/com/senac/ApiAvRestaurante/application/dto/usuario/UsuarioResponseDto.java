package com.senac.ApiAvRestaurante.application.dto.usuario;

import com.senac.ApiAvRestaurante.domain.entities.Usuario;

public record UsuarioResponseDto(Long id, String nome, String email, String cpf) {

    public UsuarioResponseDto(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf()
        );
    }
}
