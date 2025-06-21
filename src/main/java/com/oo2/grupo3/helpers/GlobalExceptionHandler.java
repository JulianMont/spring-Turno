package com.oo2.grupo3.helpers;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public String handleEntidadNoEncontrada(EntidadNoEncontradaException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        // Aquí retornás la vista Thymeleaf para error 404 o recurso no encontrado
        return "error/404";
    }

    @ExceptionHandler(ErrorValidacionDatosException.class)
    public String handleErrorValidacionDatos(ErrorValidacionDatosException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        // Vista para errores de validación de negocio
        return "error/400";
    }

   
}
