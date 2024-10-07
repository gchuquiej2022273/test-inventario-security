package com.is4tech.base.service.impl;

import com.is4tech.base.dto.EmailDto;
import com.is4tech.base.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(EmailDto email, String token) throws MessagingException {
            try{
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(email.getEmail());
                helper.setFrom("brandomgomez59@gmail.com", "InventarioCorp");
                helper.setSubject("[Inventario] please, reset your password :D");

                Context context = new Context();
                context.setVariable("encabezado", "Código de recuperación de contraseña es : ");
                context.setVariable("token", token);
                context.setVariable("cuerpo", "Si usted no solicitó la recuperación de contraseña notifique al administrador");

                String contentHTML = templateEngine.process("email", context);

                helper.setText(contentHTML, true);

                javaMailSender.send(message);

            }catch (Exception e){
                throw new RuntimeException("Error sending recovery email" + e.getMessage());
            }

    }

    @Override
    public void sendEmail(String email, String name, String surname, String randomPassword) throws MessagingException {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom("brandomgomez59@gmail.com", "InventarioCorp");
            helper.setSubject("¡Bienvenido(a) a InventarioCorp, " + name + "!");

            Context context = new Context();
            context.setVariable("encabezado", "¡Bienvenido(a) a [Inventario Corp], " + name + "!");
            context.setVariable("name", name);
            context.setVariable("passwordasig", "Tu contraseña asignada es la siguiente:");
            context.setVariable("password", randomPassword);

            String contentHTML = templateEngine.process("emailnewuser", context);

            helper.setText(contentHTML, true);

            javaMailSender.send(message);

        }catch (Exception e){
            throw new RuntimeException("Error sending random password" + e.getMessage());
        }
    }

}
