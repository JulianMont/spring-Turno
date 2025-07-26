package com.oo2.grupo3.restcontrollers;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Turnos")
                        .version("1.0")
                        .description("Documentación de la API REST para gestionar el proyecto de turnos en Spring para OO2")
                        .contact(new Contact()
                                .name("Grupo 3")
                                .email("matiasspringorueta@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
