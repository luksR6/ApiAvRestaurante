package com.senac.ApiAvRestaurante.application.dto.usuario;

public record UsuarioRequestDto(Long id, String nome, String email, String cpf, String senha, String role) {
}
