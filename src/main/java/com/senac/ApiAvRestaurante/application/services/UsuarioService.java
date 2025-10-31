package com.senac.ApiAvRestaurante.application.services;

import com.senac.ApiAvRestaurante.application.dto.login.EsqueciMinhaSenhaDto;
import com.senac.ApiAvRestaurante.application.dto.login.LoginRequestDto;
import com.senac.ApiAvRestaurante.application.dto.login.RegistrarNovaSenhaDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioRequestDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioResponseDto;
import com.senac.ApiAvRestaurante.domain.entities.Usuario;
import com.senac.ApiAvRestaurante.domain.interfaces.IEnvioEmail;
import com.senac.ApiAvRestaurante.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IEnvioEmail iEnvioEmail;

    public boolean validarSenha(LoginRequestDto login){

        return usuarioRepository.existsUsuarioByEmailContainingAndSenha(login.email(), login.senha());
    }

    public UsuarioResponseDto consultarPorId(Long id){

        return usuarioRepository.findById(id).map(UsuarioResponseDto::new).orElse(null);
    }

    public List<UsuarioResponseDto> consultarTodosSemFiltro(){
        return usuarioRepository.findAll().stream().map(UsuarioResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDto salvarUsario(UsuarioRequestDto usuarioRequest){

        var usuario = usuarioRepository.findByCpf(usuarioRequest.cpf()).map(u -> {
            u.setNome(usuarioRequest.nome());
            u.setSenha(usuarioRequest.senha());
            u.setRole(usuarioRequest.role());
            u.setEmail(usuarioRequest.email());
            return u;
        })
                .orElse(new Usuario(usuarioRequest));


        usuarioRepository.save(usuario);

        return usuario.toResponseDto();

    }

    public List<UsuarioResponseDto> consultarPaginadoFiltrado(Long take, Long page, String filtro) {

        return usuarioRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Usuario :: getNome).reversed()) // eu quero ordenar pelo ID
                .filter(p -> p.getDataCadastro().isAfter(LocalDateTime.now().plusDays(-7)))
                .filter(a -> filtro != null ? a.getNome().contains(filtro) : true)
                .skip((long)page * take)
                .limit(take)
                .map(UsuarioResponseDto::new)
                .collect(Collectors.toList());

    }

    public void recuperarSenhaEnvio(UsuarioPrincipalDto usuarioLogado) {

        iEnvioEmail.enviarEmailSimples("luscamr6@gmail.com",
                "Código recuperação",
                "123456"
        );
    }

    public String gerarCodigoAleatorio(int length) {

        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            senha.append(CHARS.charAt(randomIndex));
        }
        return senha.toString();
    }

    public void esqueciMinhaSenha(EsqueciMinhaSenhaDto esqueciMinhaSenhaDto){

        var usuario = usuarioRepository.findByEmail(esqueciMinhaSenhaDto.email()).orElse(null);

        if (usuario != null){
            var codigo = gerarCodigoAleatorio(6);

            usuario.setTokenSenha(codigo);

            usuarioRepository.save(usuario);


        iEnvioEmail.enviarEmailComTemplate(esqueciMinhaSenhaDto.email(),
                "Codigo Recuperacao",
                codigo
            );
        }
    }

    public void registrarNovaSenha(RegistrarNovaSenhaDto registrarNovaSenhaDto) {

        var usuario = usuarioRepository.findByEmailAndTokenSenha(
                registrarNovaSenhaDto.email(),
                registrarNovaSenhaDto.token())
                .orElse(null);

        if (usuario != null){

            usuario.setSenha(registrarNovaSenhaDto.senha());
            usuarioRepository.save(usuario);
        }
        }
    }

