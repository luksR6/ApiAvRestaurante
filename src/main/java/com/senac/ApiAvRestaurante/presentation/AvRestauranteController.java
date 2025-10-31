package com.senac.ApiAvRestaurante.presentation;

import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoRequestDto;
import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoResponseDto;
import com.senac.ApiAvRestaurante.application.services.AvRestauranteService;
import com.senac.ApiAvRestaurante.domain.entities.Avaliacao;
import com.senac.ApiAvRestaurante.domain.repository.AvRestauranteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/avaliacao")
@Tag(name = "Controlador de avaliação", description = "Camada responsável por controlar as avaliações dos restaurantes")
public class AvRestauranteController {

//    @Autowired
//    private AvRestauranteRepository avRestauranteRepository;

    @Autowired
    private AvRestauranteService avRestauranteService;

//    @GetMapping("/{id}")
//    @Operation(summary = "Consultar avaliação por Id", description = "Metodo responsável para consultar avaliação por Id")
//    public ResponseEntity<Avaliacao> consultaAvPorId(@PathVariable Long id){
//
//        Optional<Avaliacao> optionalAvaliacao = avRestauranteRepository.findById(id);
//
//        if (optionalAvaliacao == null){
//            return ResponseEntity.notFound().build();
//        }
//
//        Avaliacao avaliacao = optionalAvaliacao.get();
//
//        return ResponseEntity.ok(avaliacao);
//    }

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
    public ResponseEntity<AvaliacaoResponseDto> cadastrarAvalaicao(@Valid @RequestBody AvaliacaoRequestDto avaliacaoDto){

        try {
            var avaliacaoResponse = avRestauranteService.salvarAvaliacao(avaliacaoDto);

            return ResponseEntity.ok(avaliacaoResponse);
        } catch (Exception e){
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

//    @DeleteMapping("/{id}")
//    @Operation(summary = "Deletar avaliação", description = "Metodo responsável por deletar avaliações")
//    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id){
//        if (!avRestauranteRepository.existsById(id)){
//            throw new RuntimeException("Avaliação não encontrada");
//        }
//
//        try {
//            avRestauranteRepository.deleteById(id);
//            return ResponseEntity.ok(null);
//        } catch (Exception e){
//            return ResponseEntity.badRequest().build();
//        }
//    }

}
