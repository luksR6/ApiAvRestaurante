package com.senac.ApiAvRestaurante.application.services;

import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoRequestDto;
import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoResponseDto;
import com.senac.ApiAvRestaurante.domain.entities.Avaliacao;
import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import com.senac.ApiAvRestaurante.domain.repository.AvRestauranteRepository;
import com.senac.ApiAvRestaurante.domain.repository.RestauranteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<AvaliacaoResponseDto> consultarTodasAvSemFiltro(){
        return avRestauranteRepository.findAll().stream().map(Avaliacao::toResponseDto).collect(Collectors.toList());
    }

    public AvaliacaoResponseDto consultarAvaliacaoPorId(Long id){

        return avRestauranteRepository.findById(id).map(Avaliacao::toResponseDto).orElse(null);
    }

    @Transactional
    public AvaliacaoResponseDto salvarAvaliacao(AvaliacaoRequestDto dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.restauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante com ID " + dto.restauranteId() + " não encontrado!"));

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setNota(dto.nota());
        novaAvaliacao.setComentario(dto.comentario());
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now());
        novaAvaliacao.setRestaurante(restaurante);

        avRestauranteRepository.save(novaAvaliacao);

        Double novaMedia = avRestauranteRepository.calcularMediaPorRestauranteId(restaurante.getId());
        Double mediaFinal = (novaMedia != null ? novaMedia : 0.0);

        restaurante.setMediaNota(mediaFinal);

        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        novaAvaliacao.setRestaurante(restauranteAtualizado);


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
}

