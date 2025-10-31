package com.senac.ApiAvRestaurante.presentation;

import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteRequestDto;
import com.senac.ApiAvRestaurante.application.dto.restaurante.RestauranteResponseDto;
import com.senac.ApiAvRestaurante.application.services.RestauranteService;
import com.senac.ApiAvRestaurante.domain.entities.Avaliacao;
import com.senac.ApiAvRestaurante.domain.entities.Restaurante;
import com.senac.ApiAvRestaurante.domain.repository.RestauranteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Controlador de Restaurante", description = "Camada responsável por controlar os restaurantes cadastrados")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar Restaurantes por Id", description = "Metodo responsável por consultar restaurantes cadastrados por ID")
    public ResponseEntity<RestauranteResponseDto> consultarRestaurantePorId(@PathVariable Long id) {

        var restaurante = restauranteService.consultarPorId(id);

        if (restaurante == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(restaurante);
    }

    @GetMapping
    @Operation(summary = "Consultar Todos Restaurantes", description = "Metodo responsável por consultar todos os restaurantes cadastrados")
    public ResponseEntity<List<RestauranteResponseDto>> consultarTodos(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(restauranteService.consultarTodos());
    }

    @PostMapping
    @Operation(summary = "Salvar Restaurante", description = "Metodo responsável por cadastrar e salvar restaurantes")
    public ResponseEntity<RestauranteResponseDto> salvarRestaurante(@RequestBody RestauranteRequestDto restaurante) {

        try{
           var restauranteResponse = restauranteService.salvarRestaurante(restaurante);

           return ResponseEntity.ok(restauranteResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar avaliação", description = "Metódo responsável por atualizar os restaurantes")
    public ResponseEntity<RestauranteResponseDto> atualizarRestaurantes(
            @RequestBody @Valid RestauranteRequestDto restauranteRequestDto,
            @PathVariable Long id) {

        RestauranteResponseDto restauranteResponseDto = restauranteService.atualizarRestaurante(id, restauranteRequestDto);
        return ResponseEntity.ok(restauranteResponseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar restaurante", description = "Metodo responsável por deletar restaurantes")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {

        try {
            restauranteService.deletarRestaurante(id);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
