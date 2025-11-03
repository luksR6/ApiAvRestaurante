package com.senac.ApiAvRestaurante.presentation;

import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioRequestDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioResponseDto;
import com.senac.ApiAvRestaurante.application.services.UsuarioService;
import com.senac.ApiAvRestaurante.domain.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Controlador de usuário", description = "Camada responsável por controlar usuário que vão avaliar os restaurantes")
public class UsuarioController {

    // numa aplicação nunca irá ter um delete, somente um dado muda de estado

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar Usuários por Id", description = "Metodo responsável por consultar usuários por Id")
    public ResponseEntity<UsuarioResponseDto> consultaPorId(@PathVariable Long id) {

        var usuario = usuarioService.consultarPorId(id);

        //SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //recuperar o usuario e as informações

        if (usuario == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "Consultar Usuários", description = "Metodo responsável por consultar todos os usuários")
    public ResponseEntity<List<UsuarioResponseDto>> consultarTodos(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(usuarioService.consultarTodosSemFiltro());
    }

    @GetMapping("/grid")
    @Operation(summary = "Usuarios por filtros", description = "Metodo responsável por consultar dados do usuario paginado e com filtros ")
    public ResponseEntity<List<UsuarioResponseDto>> consultarPaginadoFiltrado(@Parameter(description = "Parametro de quantidade de registro por pagina") @RequestParam Long take,
                                                                              @Parameter(description = "Parametro de quantidade de pagina")@RequestParam Long page,
                                                                              @Parameter(description = "Parametro de filtro") @RequestParam(required = false) String filtro){

        return ResponseEntity.ok(usuarioService.consultarPaginadoFiltrado(take, page,filtro));
    }

    @PostMapping
    @Operation(summary = "Salvar Usuario", description = "Metodo responsável por salvar usuario")
    public ResponseEntity<UsuarioResponseDto> salvarUsuario(@RequestBody UsuarioRequestDto usuario){

        try {
            var usuarioResponse = usuarioService.salvarUsario(usuario);

            return ResponseEntity.ok(usuarioResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/admin-normal")
    @Operation(summary = "Criar Admin de Restaurante", description = "Metodo responsável por criar o admin de restaurante")
    public ResponseEntity<UsuarioResponseDto> criarAdminNormal(@RequestBody UsuarioRequestDto adminRequest) {
        try {
            var usuarioResponse = usuarioService.criarAdminNormal(adminRequest);

            return ResponseEntity.ok(usuarioResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
