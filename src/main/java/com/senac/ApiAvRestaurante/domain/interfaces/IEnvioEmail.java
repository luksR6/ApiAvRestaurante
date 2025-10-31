package com.senac.ApiAvRestaurante.domain.interfaces;

public interface IEnvioEmail {

    void enviarEmailSimples(String para, String assunto, String texto);
    void enviarEmailComTemplate(String para, String assunto, String texto);
}
