package com.senac.ApiAvRestaurante.controllers;

import com.senac.ApiAvRestaurante.dto.AvaliacaoRequestDto;
import com.senac.ApiAvRestaurante.model.Avaliacao;
import com.senac.ApiAvRestaurante.repository.AvRestauranteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/avaliacao")
@Tag(name = "Controlador de avaliação", description = "Camada responsável por controlar as avaliações dos restaurantes")
public class AvRestauranteController {

    @Autowired
    private AvRestauranteRepository avRestauranteRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar avaliação por Id", description = "Metodo responsável para consultar avaliação por Id")
    public ResponseEntity<Avaliacao> consultaAvPorId(@PathVariable Long id){

        var avaliacao = avRestauranteRepository.findById(id).orElse(null);

        if (avaliacao == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(avaliacao);
    }

    @GetMapping
    @Operation(summary = "Consultar as avaliações", description = "Metodo responsável por consultar todas as avaliações")
    public ResponseEntity<?> consultarTodasAv(){

        return ResponseEntity.ok(avRestauranteRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Cadastrar avaliação", description = "Metodo responsável por cadastrar avaliações")
    public ResponseEntity<?> cadastrarAvalaicao(@RequestBody AvaliacaoRequestDto requestDto){

        try {
            var avaliacaoResponse = requestDto.transformarEmEntidade();
            avRestauranteRepository.save(avaliacaoResponse);

            return ResponseEntity.ok(avaliacaoResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar avaliação", description = "Metodo responsável por atualizar avaliações")
    public ResponseEntity<?> atualizarAvaliacao(@RequestBody AvaliacaoRequestDto avaliacaoDto,@PathVariable Long id){
        if (!avRestauranteRepository.existsById(id)){
            throw new RuntimeException("Avaliação não encontrada");
        }

        Optional<Avaliacao> optionalAvaliacao = avRestauranteRepository.findById(id);
        var avaliacao = optionalAvaliacao.get();

        avaliacao.setComentario(avaliacaoDto.comentario());
        avaliacao.setNota(avaliacaoDto.nota());
        avaliacao.setNomeRestaurante(avaliacaoDto.nomeRestaurante());
        avRestauranteRepository.save(avaliacao);

        return ResponseEntity.ok(avaliacao);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar avaliação", description = "Metodo responsável por deletar avaliações")
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id){
        if (!avRestauranteRepository.existsById(id)){
            throw new RuntimeException("Avaliação não encontrada");
        }

        try {
            avRestauranteRepository.deleteById(id);
            return ResponseEntity.ok(null);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
