package com.senac.ApiAvRestaurante.controllers;

import com.senac.ApiAvRestaurante.model.Avaliacao;
import com.senac.ApiAvRestaurante.repository.AvRestauranteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avalicao")
@Tag(name = "Controlador de avaliação", description = "Camada responsável por controlar as avaliações dos restaurantes")
public class AvRestauranteController {

    @Autowired
    private AvRestauranteRepository avRestauranteRepository;

    @GetMapping("/id")
    @Operation(summary = "avaliacao", description = "Metodo responsável para consultar avaliação por Id")
    public ResponseEntity<Avaliacao> consultaAvPorId(@PathVariable Long id){

        var avalicao = avRestauranteRepository.findById(id).orElse(null);

        if (avalicao == null{
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(avalicao);
    }

    @GetMapping
    @Operation(summary = "avaliacao", description = "Metodo responsável por consultar todas as avaliações")
    public ResponseEntity<?> consultarTodasAv(){

        return ResponseEntity.ok(avRestauranteRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Salvar avaliação", description = "Metodo responsável por salvar usuario")
    public ResponseEntity<?> salvarAvalicao(@RequestBody Avaliacao avaliacao){

        try {
            var avaliacaoResponse = avRestauranteRepository.save(avaliacao);

            return ResponseEntity.ok(avaliacaoResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


}
