// Local: domain/entities/Avaliacao.java
package com.senac.ApiAvRestaurante.domain.entities;

import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoResponseDto;
import com.senac.ApiAvRestaurante.application.dto.usuario.UsuarioResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    public AvaliacaoResponseDto toResponseDto() {

        UsuarioResponseDto usuarioDto = null;

        if (this.usuario != null) {
            usuarioDto = this.usuario.toResponseDto();
        }

        return new AvaliacaoResponseDto(
                this.id,
                this.nota,
                this.comentario,
                this.restaurante.getNome(),
                this.restaurante.getMediaNota(),
                usuarioDto
        );
    }
}