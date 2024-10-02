package com.is4tech.base.service.impl;

import com.is4tech.base.dto.EmailDto;
import com.is4tech.base.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailServiceImpl implements EmailService {

    JavaMailSender javaMailSender;

    TemplateEngine templateEngine;


    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

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
                context.setVariable("button-text", "Recuperar contraseña");
                context.setVariable("footer-one", "Este correo fue enviado automáticamente. Por favor, no respondas a este mensaje.");
                context.setVariable("footer-two", "&copy; 2024 InventarioCompany sociedad anonima. Todos los derechos reservados.");

                String contentHTML = templateEngine.process("email", context);

                helper.setText(contentHTML, true);

                javaMailSender.send(message);

            }catch (Exception e){
                throw new RuntimeException("Error al enviar el email " + e.getMessage());
            }

    }

}
