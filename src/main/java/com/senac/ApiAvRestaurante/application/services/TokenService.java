package com.senac.ApiAvRestaurante.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.senac.ApiAvRestaurante.application.dto.login.LoginRequestDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.ApiAvRestaurante.domain.entities.Token;
import com.senac.ApiAvRestaurante.domain.entities.Usuario;
import com.senac.ApiAvRestaurante.domain.repository.TokenRepository;
import com.senac.ApiAvRestaurante.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.tempo_expiracao}")
    private Long tempo;

    private String emissor = "AvRestaurante";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String gerarToken(LoginRequestDto loginRequestDto){

        var usuario = usuarioRepository.findByEmail(loginRequestDto.email()).orElse(null);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Algorithm algorithm = Algorithm.HMAC256(secret);

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario.getEmail())
                .withExpiresAt(this.gerarDataExpiracao())
                .withClaim("roles", roles)
                .sign(algorithm);

        tokenRepository.save(new Token(null, token, usuario));
        return token;
    }

    private Instant gerarDataExpiracao(){

        var dataAtual = LocalDateTime.now();
        dataAtual = dataAtual.plusMinutes(tempo);

        return dataAtual.toInstant(ZoneOffset.of("-03:00"));

    }

    public UsuarioPrincipalDto validarToken(String token){

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(emissor)
                .build();
        verifier.verify(token);

        var tokenResult = tokenRepository.findByToken(token).orElse(null);

        if (tokenResult == null){
            throw new IllegalArgumentException("Token inválido");
        }

        return new UsuarioPrincipalDto(tokenResult.getUsuario());
    }

}