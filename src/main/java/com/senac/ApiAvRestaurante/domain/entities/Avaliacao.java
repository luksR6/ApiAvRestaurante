// Local: domain/entities/Avaliacao.java
package com.senac.ApiAvRestaurante.domain.entities;

import com.senac.ApiAvRestaurante.application.dto.avaliacao.AvaliacaoResponseDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    public AvaliacaoResponseDto toResponseDto() {
        return new AvaliacaoResponseDto(
                this.id,
                this.nota,
                this.comentario,
                // Pega o nome do objeto Restaurante que está ligado a esta avaliação
                this.restaurante.getNome(),
                // Pega a média de notas do objeto Restaurante
                this.restaurante.getMediaNota()
        );
    }
}