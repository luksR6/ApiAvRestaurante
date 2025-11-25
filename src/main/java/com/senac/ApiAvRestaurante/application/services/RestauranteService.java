package com.senac.ApiAvRestaurante.application.services;

import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteRequestDto;
import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteResponseDto;
import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import com.senac.ApiAvRestaurante.domain.entities.Usuario;
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
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public RestauranteResponseDto consultarPorId(Long id) {

        return restauranteRepository.findById(id).map(RestauranteResponseDto::new).orElse(null);
    }

    public List<RestauranteResponseDto> listarPorNome(String nome) {
        Usuario usuario = usuarioService.buscarUsuarioLogado();

        List<Restaurante> restaurantes;

        if (usuario.getRole().equals("ROLE_ADMIN_GERAL")) {
            restaurantes = restauranteRepository.findAll();
        } else if (usuario.getRole().equals("ROLE_ADMIN_NORMAL")) {
            restaurantes = restauranteRepository.findByAdminId(usuario.getId());
        } else {
            restaurantes = restauranteRepository.findAll();
        }

        if (nome != null && !nome.isBlank()) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        return restaurantes.stream()
                .map(RestauranteResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RestauranteResponseDto> consultarTodos(){
        return restauranteRepository.findAll().stream().map(RestauranteResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public RestauranteResponseDto salvarRestaurante(RestauranteRequestDto dto) {

        Usuario adminLogado = usuarioService.buscarUsuarioLogado();

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.nome());
        restaurante.setAdmin(adminLogado);

        var salvo = restauranteRepository.save(restaurante);
        return new RestauranteResponseDto(salvo);
    }


    @Transactional
    public RestauranteResponseDto atualizarRestaurante(Long id, RestauranteRequestDto dto) {

        Usuario adminLogado = usuarioService.buscarUsuarioLogado();

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        if (!restaurante.getAdmin().getId().equals(adminLogado.getId())) {
            throw new RuntimeException("Você não tem permissão para alterar este restaurante");
        }

        restaurante.setNome(dto.nome());
        var atualizado = restauranteRepository.save(restaurante);

        return new RestauranteResponseDto(atualizado);
    }


    public void deletarRestaurante(Long id) {

        Usuario adminLogado = usuarioService.buscarUsuarioLogado();

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        if (!restaurante.getAdmin().getId().equals(adminLogado.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este restaurante");
        }

        restauranteRepository.delete(restaurante);
    }

    public List<RestauranteResponseDto> listarRestaurantesDoAdmin() {
        Usuario adminLogado = usuarioService.buscarUsuarioLogado();

        var restaurantes = restauranteRepository.findByAdminId(adminLogado.getId());

        return restaurantes.stream()
                .map(RestauranteResponseDto::new)
                .toList();
    }


}
