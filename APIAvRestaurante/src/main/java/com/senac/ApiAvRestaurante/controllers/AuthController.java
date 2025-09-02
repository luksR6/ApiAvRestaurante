package com.senac.ApiAvRestaurante.controllers;

import com.senac.ApiAvRestaurante.dto.LoginRequestDto;
import com.senac.ApiAvRestaurante.model.Usuario;
import com.senac.ApiAvRestaurante.services.TokenService;
import com.senac.ApiAvRestaurante.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Controller autenticação", description = "Controller para verificar a autenticação")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "login", description = "Metodo responsável em efutar o login do usuario")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request){

        if (!usuarioService.validarSenha(request)){
            return ResponseEntity.badRequest().body("Usuario ou senha invalido");
        }
        var token = tokenService.gerarToken(request.email(), request.senha());

        return ResponseEntity.ok(token);
    }
}
