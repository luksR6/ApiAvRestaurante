// Local: application/dto/avaliacao/AvaliacaoRequestDto.java
package com.senac.ApiAvRestaurante.application.dto.avaliacao;

import jakarta.validation.constraints.*;

public record AvaliacaoRequestDto(
        @NotNull(message = "O ID do restaurante é obrigatório.")
        Long restauranteId,

        // SUAS VALIDAÇÕES DE NOTA ESTÃO AQUI
        @Min(value = 1, message = "A nota não pode ser menor que 1.")
        @Max(value = 5, message = "A nota não pode ser maior que 5.")
        int nota,

        // SUAS VALIDAÇÕES DE COMENTÁRIO ESTÃO AQUI
        @NotBlank(message = "O comentário não pode estar em branco.")
        @Size(max = 500, message = "O comentário não pode exceder 500 caracteres.")
        String comentario
) {
}