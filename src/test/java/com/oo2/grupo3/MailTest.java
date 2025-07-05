package com.oo2.grupo3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.oo2.grupo3.services.implementations.EmailService;
@SpringBootApplication
public class MailTest {

    @Autowired
    private EmailService mailService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MailTest.class, args);
        MailTest app = context.getBean(MailTest.class);
        app.run(); // Usamos un método no estático
    }

    public void run() {
        mailService.enviarMailTest();
    }
}



