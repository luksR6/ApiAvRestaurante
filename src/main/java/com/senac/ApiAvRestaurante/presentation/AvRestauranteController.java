package com.senac.ApiAvRestaurante.presentation;

import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoRequestDto;
import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoResponseDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.ApiAvRestaurante.application.services.AvRestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/avaliacao")
@Tag(name = "Controlador de avaliação", description = "Camada responsável por controlar as avaliações dos restaurantes")
public class AvRestauranteController {

//    @Autowired
//    private AvRestauranteRepository avRestauranteRepository;

    @Autowired
    private AvRestauranteService avRestauranteService;

    @GetMapping("/{id}") // 1. O caminho agora espera um ID
    @Operation(summary = "Consultar avaliações por Id", description = "Metodo responsável por consultar as avaliações de um restaurante específico")
    public ResponseEntity<AvaliacaoResponseDto> consultarAvaliacaoPorId(@PathVariable Long id) {

        try {
            AvaliacaoResponseDto avaliacaoResponseDto = avRestauranteService.consultarAvaliacaoPorId(id);
            return ResponseEntity.ok(avaliacaoResponseDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Consultar avaliações por ID do Restaurante")
    public ResponseEntity<List<AvaliacaoResponseDto>> consultarPorRestauranteId(@PathVariable Long restauranteId){

        List<AvaliacaoResponseDto> avaliacoes = avRestauranteService.consultarPorRestaurante(restauranteId);

        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping
    @Operation(summary = "Consultar as avaliações", description = "Metodo responsável por consultar todas as avaliações")
    public ResponseEntity<List<AvaliacaoResponseDto>> consultarTodasAv(){

        return ResponseEntity.ok(avRestauranteService.consultarTodasAvSemFiltro());
    }

    @GetMapping("/grid")
    @Operation(summary = "Avaliações por filtros", description = "Metodo responsável por consultar dados da avaliações paginado com filtros")
    public ResponseEntity<List<AvaliacaoResponseDto>> consultarPaginadoFiltrado(@Parameter(description = "Parametro de quantidade de registro por pagina") @RequestParam Long take,
                                                                                @Parameter(description = "Parametro de quantidade de pagina")@RequestParam Long page,
                                                                                @Parameter(description = "Parametro de filtro") @RequestParam(required = false) String filtro){

        return ResponseEntity.ok(avRestauranteService.consultarPaginadoFiltrado(take, page, filtro));
    }

    @PostMapping
    @Operation(summary = "Cadastrar avaliação", description = "Metodo responsável por cadastrar avaliações")
    public ResponseEntity<AvaliacaoResponseDto> cadastrarAvaliacao(@Valid @RequestBody AvaliacaoRequestDto avaliacaoDto) {
        try {
            AvaliacaoResponseDto response = avRestauranteService.salvarAvaliacao(avaliacaoDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar avaliação", description = "Método responsável por atualizar avaliações")
    public ResponseEntity<AvaliacaoResponseDto> atualizarAvaliacao(
            @RequestBody @Valid AvaliacaoRequestDto avaliacaoDto,
            @PathVariable Long id) {

        AvaliacaoResponseDto avaliacaoResponseDto = avRestauranteService.atualizarAvaliacao(id, avaliacaoDto);
        return ResponseEntity.ok(avaliacaoResponseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar avaliação", description = "Deleta uma avaliação se pertencer ao usuário logado")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable Long id) {
        try {
            avRestauranteService.deletarAvaliacao(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Minhas Avaliações", description = "Retorna apenas as avaliações do usuário logado")
    public ResponseEntity<List<AvaliacaoResponseDto>> consultarMinhasAvaliacoes() {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UsuarioPrincipalDto principal = (UsuarioPrincipalDto) auth.getPrincipal();

            Long idUsuario = principal.id();

            List<AvaliacaoResponseDto> lista = avRestauranteService.listarMinhasAvaliacoes(idUsuario);

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
