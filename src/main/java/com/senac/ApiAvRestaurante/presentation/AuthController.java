package com.senac.ApiAvRestaurante.presentation;

import com.senac.ApiAvRestaurante.application.dto.login.*;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.ApiAvRestaurante.application.services.TokenService;
import com.senac.ApiAvRestaurante.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponseCompletoDto> login(@RequestBody LoginRequestDto request){

        try {
            LoginResponseCompletoDto resposta = usuarioService.autenticarLogin(request);
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/recuperarsenha/envio")
    @Operation(summary = "Recuperar senha" , description = "Metodo de envio de e-mail para recuperar senha")
    public ResponseEntity<?> recuperarSenha(@AuthenticationPrincipal UsuarioPrincipalDto usuarioLogado){

        try {
            usuarioService.recuperarSenhaEnvio(usuarioLogado);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/esqueciMinhaSenha")
    @Operation(summary = "Esqueci Minha Senha", description = "Método responsável por recuperar a senha")
    public ResponseEntity<?> esqueciMinhaSenha (@RequestBody EsqueciMinhaSenhaDto esqueciMinhaSenhaDTO) {
        try{
            usuarioService.esqueciMinhaSenha(esqueciMinhaSenhaDTO);

            return ResponseEntity.ok().build();
        }catch (Exception e){

            return ResponseEntity.badRequest().build();


        }
    }

    @PostMapping("/registrarnovasenha")
    @Operation(summary = "Registrar nova senha", description = "Metodo para registrar uma nova senha quando esquecer")
    public ResponseEntity<?> registrarNovaSenha(@RequestBody RegistrarNovaSenhaDto registrarNovaSenhaDto){


        try {

            usuarioService.registrarNovaSenha(registrarNovaSenhaDto);

            return ResponseEntity.ok().build();

        } catch (Exception e){
            ResponseEntity.badRequest().build();
        }
        return null;
    }

}

