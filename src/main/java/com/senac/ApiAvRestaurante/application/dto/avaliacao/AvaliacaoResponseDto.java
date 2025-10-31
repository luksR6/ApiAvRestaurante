// Local: application/dto/avaliacao/AvaliacaoResponseDto.java
package com.senac.ApiAvRestaurante.application.dto.avaliacao;

// Vamos usar um record, que é mais moderno e limpo para DTOs.
// Ele reflete os dados que queremos ENVIAR DE VOLTA para o front-end.
public record AvaliacaoResponseDto(
        Long id,
        int nota,
        String comentario,
        String nomeRestaurante,
        Double mediaNotaDoRestaurante // A nova média do restaurante, não da avaliação.
) {
    // Com 'record', o construtor é criado automaticamente. Não precisamos de mais nada aqui.
}