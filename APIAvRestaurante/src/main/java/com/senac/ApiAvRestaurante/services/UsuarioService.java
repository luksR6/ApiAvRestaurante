package com.senac.ApiAvRestaurante.services;

import com.senac.ApiAvRestaurante.dto.LoginRequestDto;
import com.senac.ApiAvRestaurante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarSenha(LoginRequestDto login){

        return usuarioRepository.existsUsuarioByEmailContainingAndSenha(login.email(), login.senha());
    }
}
