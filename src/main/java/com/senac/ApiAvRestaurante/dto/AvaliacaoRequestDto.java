package com.senac.ApiAvRestaurante.dto;

import com.senac.ApiAvRestaurante.model.Avaliacao;

public record AvaliacaoRequestDto(String nomeRestaurante,
                                  int nota,
                                  String comentario) {

    public Avaliacao transformarEmEntidade() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNomeRestaurante(nomeRestaurante);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);

        return avaliacao;
    }
}