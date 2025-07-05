package com.oo2.grupo3.services.implementations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String para, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }
    
    public void enviarMailTest() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("matiasorueta2000@gmail.com"); // poné tu mail real para pruebas
        message.setSubject("Prueba de correo");
        message.setText("Este es un correo de prueba desde Spring Boot.");
        mailSender.send(message);
        System.out.println("Mail de prueba enviado.");
    }
}
