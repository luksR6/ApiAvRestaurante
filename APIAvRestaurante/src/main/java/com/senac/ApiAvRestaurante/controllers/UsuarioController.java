package com.senac.ApiAvRestaurante.controllers;

import com.senac.ApiAvRestaurante.model.Usuario;
import com.senac.ApiAvRestaurante.repository.AvRestauranteRepository;
import com.senac.ApiAvRestaurante.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Controlador de usuário", description = "Camada responsável por controlar usuário que vão avaliar os restaurantes")
public class UsuarioController {

    // numa aplicação nunca irá ter um delete, somente um dado muda de estado

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/{id}")
    @Operation(summary = "usuario", description = "Metodo responsável por consultar usuários por Id")
    public ResponseEntity<Usuario> consultaPorId(@PathVariable Long id) {

        var usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "usuario", description = "Metodo responsável por consultar todos os usuários")
    public ResponseEntity<?> consultarTodos(){

        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Salvar Usuario", description = "Metodo responsável por salvar usuario")
    public ResponseEntity<?> salvarUsuario(@RequestBody Usuario usuario){

        try {
            var usuarioResponse = usuarioRepository.save(usuario);

            return ResponseEntity.ok(usuarioResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
