package com.senac.ApiAvRestaurante.application.services;

import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoRequestDto;
import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoResponseDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.ApiAvRestaurante.domain.entities.Avaliacao;
import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import com.senac.ApiAvRestaurante.domain.entities.Usuario;
import com.senac.ApiAvRestaurante.domain.repository.AvRestauranteRepository;
import com.senac.ApiAvRestaurante.domain.repository.RestauranteRepository;
import com.senac.ApiAvRestaurante.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvRestauranteService {

    @Autowired
    private AvRestauranteRepository avRestauranteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<AvaliacaoResponseDto> consultarTodasAvSemFiltro(){
        return avRestauranteRepository.findAll().stream().map(Avaliacao::toResponseDto).collect(Collectors.toList());
    }

    public AvaliacaoResponseDto consultarAvaliacaoPorId(Long id){

        return avRestauranteRepository.findById(id).map(Avaliacao::toResponseDto).orElse(null);
    }

    @Transactional
    public AvaliacaoResponseDto salvarAvaliacao(AvaliacaoRequestDto dto) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();

        if (!(principal instanceof UsuarioPrincipalDto usuarioDto)) {
            throw new RuntimeException("Usuário não autenticado!");
        }

        String emailUsuario = usuarioDto.email();

        Restaurante restaurante = restauranteRepository.findById(dto.restauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco! Email=" + emailUsuario));

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setNota(dto.nota());
        novaAvaliacao.setComentario(dto.comentario());
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now());
        novaAvaliacao.setRestaurante(restaurante);
        novaAvaliacao.setUsuario(usuario);

        avRestauranteRepository.save(novaAvaliacao);

        Double novaMedia = avRestauranteRepository.calcularMediaPorRestauranteId(restaurante.getId());
        restaurante.setMediaNota(novaMedia == null ? 0.0 : novaMedia);

        restauranteRepository.save(restaurante);

        return novaAvaliacao.toResponseDto();
    }

    @Transactional
    public AvaliacaoResponseDto atualizarAvaliacao(Long id, AvaliacaoRequestDto dto) {

        Avaliacao avaliacao = avRestauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação com ID " + id + " não encontrada"));

        avaliacao.setComentario(dto.comentario());
        avaliacao.setNota(dto.nota());

        avRestauranteRepository.save(avaliacao);

        Restaurante restaurante = avaliacao.getRestaurante();
        Double novaMedia = avRestauranteRepository.calcularMediaPorRestauranteId(restaurante.getId());
        restaurante.setMediaNota(novaMedia);
        restauranteRepository.save(restaurante);

        return avaliacao.toResponseDto();
    }


    public List<AvaliacaoResponseDto> consultarPaginadoFiltrado(Long take, Long page, String filtro) {

        return avRestauranteRepository.findAll()
                .stream()

                .sorted(Comparator.comparing(avaliacao -> avaliacao.getRestaurante().getMediaNota(), Comparator.reverseOrder()))

                .filter(p -> p.getDataAvaliacao().isAfter(LocalDateTime.now().plusDays(-7)))

                .filter(a -> {
                    if (filtro == null || filtro.trim().isEmpty()) {
                        return true;
                    }
                    if (a.getRestaurante() != null && a.getRestaurante().getMediaNota() != null) {
                        return String.valueOf(a.getRestaurante().getMediaNota()).contains(filtro);
                    }
                    return false;
                })
                .skip(page * take)
                .limit(take)
                .map(Avaliacao::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AvaliacaoResponseDto> consultarPorRestaurante(Long restauranteId) {

        List<Avaliacao> avaliacoes = avRestauranteRepository.findByRestauranteId(restauranteId);

        return avaliacoes.stream()
                .map(Avaliacao::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarAvaliacao(Long id) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();

        if (!(principal instanceof UsuarioPrincipalDto usuarioDto)) {
            throw new RuntimeException("Usuário não autenticado!");
        }

        String emailUsuario = usuarioDto.email();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().toUpperCase().contains("ADMIN"));

        Avaliacao avaliacao = avRestauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        boolean isDono = avaliacao.getUsuario() != null &&
                avaliacao.getUsuario().getEmail().equalsIgnoreCase(emailUsuario);

        if (!isDono && !isAdmin) {
            throw new RuntimeException("Você não tem permissão para excluir esta avaliação.");
        }

        Restaurante restaurante = avaliacao.getRestaurante();

        avRestauranteRepository.delete(avaliacao);
        avRestauranteRepository.flush();

        Double novaMedia = avRestauranteRepository.calcularMediaPorRestauranteId(restaurante.getId());
        restaurante.setMediaNota(novaMedia == null ? 0.0 : novaMedia);

        restauranteRepository.save(restaurante);
    }

    public List<AvaliacaoResponseDto> listarMinhasAvaliacoes(Long idUsuario) {
        return avRestauranteRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(Avaliacao::toResponseDto)
                .toList();
    }
}

