package com.senac.ApiAvRestaurante.application.dto.usuario;

import com.senac.ApiAvRestaurante.domain.entities.Usuario;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UsuarioPrincipalDto(Long id, String email, Collection<? extends GrantedAuthority> authorities) {

    public UsuarioPrincipalDto(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getAuthorities()
        );
    }
}
