package com.senac.ApiAvRestaurante.application.services;

import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteRequestDto;
import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteResponseDto;
import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import com.senac.ApiAvRestaurante.domain.repository.RestauranteRepository;
import com.senac.ApiAvRestaurante.domain.repository.UsuarioRepository;
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

    public RestauranteResponseDto consultarPorId(Long id) {

        return restauranteRepository.findById(id).map(RestauranteResponseDto::new).orElse(null);
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
}
