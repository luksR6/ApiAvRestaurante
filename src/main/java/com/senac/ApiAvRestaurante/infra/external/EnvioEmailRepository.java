package com.senac.ApiAvRestaurante.infra.external;

import com.senac.ApiAvRestaurante.domain.interfaces.IEnvioEmail;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.swing.table.TableRowSorter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Component
public class EnvioEmailRepository implements IEnvioEmail {

    @Autowired
    private JavaMailSender javaMailSender;


    @Async
    public void enviarEmailSimples(String para, String assunto, String texto) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nao-responda@meusite.com");
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);
            javaMailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("Deu erro aqui");
        }
    }

    @Async
    public void enviarEmailComTemplate(String para, String assunto, String texto) {

        try{

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlTemplate = carregarTemplateEmail();


            String htmlFinal = htmlTemplate.replace("${message}", texto)
                    .replace("${dataEnvio}", String.valueOf(LocalDateTime.now()));

            helper.setFrom("nao-responda@suaempresa.com");
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(htmlFinal, true);

            javaMailSender.send(message);
        } catch (Exception e){
            throw new RuntimeException("Erro ao enviar o email");
        }

        }

        private String carregarTemplateEmail() throws IOException {
            ClassPathResource resource = new ClassPathResource("templates/email-template.html");
            byte[] bytes = Files.readAllBytes(resource.getFile().toPath());

            return new String(bytes, StandardCharsets.UTF_8);

    }
}
