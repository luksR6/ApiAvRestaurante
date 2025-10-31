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

    @Transactional
    public AvaliacaoResponseDto salvarAvaliacao(AvaliacaoRequestDto dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.restauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante com ID " + dto.restauranteId() + " não encontrado!"));

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setNota(dto.nota());
        novaAvaliacao.setComentario(dto.comentario());
        novaAvaliacao.setDataAvaliacao(LocalDateTime.now());
        novaAvaliacao.setRestaurante(restaurante);

        // 1. Salva a nova avaliação (como você já fazia)
        avRestauranteRepository.save(novaAvaliacao);


        // 2. Calcula a nova média
        Double novaMedia = avRestauranteRepository.calcularMediaPorRestauranteId(restaurante.getId());

        // 3. Atualiza o objeto restaurante em memória
        //    (Adicionei um 'null check' para segurança, caso a média retorne nula)
        restaurante.setMediaNota(novaMedia != null ? novaMedia : 0.0);

        // 4. --- ESTA É A LINHA QUE FALTAVA ---
        //    Salve o restaurante com a médiaNota atualizada!
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
}

