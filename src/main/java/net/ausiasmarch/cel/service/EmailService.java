package net.ausiasmarch.cel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendPasswordChangeLink(String toEmail, String enlace) {
        String html = "<div style='font-family: Arial, sans-serif;'>" +
                "<h2>Restablecimiento de contraseña</h2>" +
                "<p>Haz clic en el siguiente botón para cambiar tu contraseña:</p>" +
                "<a href='" + enlace + "' style='padding:10px 20px; background-color:#007bff; color:white; text-decoration:none; border-radius:5px;'>Cambiar contraseña</a>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este mensaje.</p>" +
                "</div>";

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(toEmail);
            helper.setSubject("Cambia tu contraseña");
            helper.setText(html, true);
            helper.setFrom("no-reply@tusitio.com");

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}
