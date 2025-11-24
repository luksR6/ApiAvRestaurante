package com.senac.ApiAvRestaurante.application.services;

import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteRequestDto;
import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteResponseDto;
import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import com.senac.ApiAvRestaurante.domain.repository.AvRestauranteRepository;
import com.senac.ApiAvRestaurante.domain.repository.RestauranteRepository;
import com.senac.ApiAvRestaurante.domain.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private AvRestauranteRepository avRestauranteRepository;

    public RestauranteResponseDto consultarPorId(Long id) {

        return restauranteRepository.findById(id).map(RestauranteResponseDto::new).orElse(null);
    }

    public List<RestauranteResponseDto> listarPorNome(String nome) {

        List<Restaurante> lista;

        if (nome != null && !nome.isEmpty()) {
            lista = restauranteRepository.findByNomeContainingIgnoreCase(nome);
        } else {
            lista = restauranteRepository.findAll();
        }

        return lista.stream().map(restaurante -> {

            Double media = avRestauranteRepository.calcularMediaPorRestauranteId(restaurante.getId());

            if (media == null) media = 0.0;


            return new RestauranteResponseDto(
                    restaurante.getId(),
                    restaurante.getNome(),
                    media
            );

        }).collect(Collectors.toList());
    }

    public List<RestauranteResponseDto> consultarTodos(){
        return restauranteRepository.findAll().stream().map(RestauranteResponseDto::new).collect(Collectors.toList());
    }

   @Transactional
   public RestauranteResponseDto salvarRestaurante(RestauranteRequestDto restauranteRequest) {
       Restaurante restaurante = new Restaurante();

       restaurante.setNome(restauranteRequest.nome());

       Restaurante restauranteSalvo = restauranteRepository.save(restaurante);

       return new RestauranteResponseDto(restauranteSalvo);
   }

    public RestauranteResponseDto atualizarRestaurante(Long id, RestauranteRequestDto restauranteRequestDto) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante com ID " + id + " não encontrada"));

        restaurante.setNome(restauranteRequestDto.nome());

        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);

        return new RestauranteResponseDto(restauranteAtualizado);
    }


   public void deletarRestaurante(Long id){

       if (!restauranteRepository.existsById(id)) {
           throw new RuntimeException("Restaurante não encontrado");
       }

       try {
           restauranteRepository.deleteById(id);
       } catch (Exception e) {
           throw new RuntimeException("Não foi possível deletar");
       }
   }
}
