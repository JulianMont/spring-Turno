package com.oo2.grupo3.helpers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.helpers.exceptions.HorarioNoDisponibleException;
import com.oo2.grupo3.helpers.exceptions.TurnoOcupadoException;

@ControllerAdvice
public class HandlerException {


	@ExceptionHandler(TurnoOcupadoException.class)

    public String manejarTurnoOcupado(TurnoOcupadoException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/turnoOcupado";
    }


    @ExceptionHandler(HorarioNoDisponibleException.class)
    public String handleHorarioNoDisponibleException(HorarioNoDisponibleException ex, Model model) {
        model.addAttribute("errorMensaje", ex.getMessage());
        return "error/horarioNoDisponible";  // Otra vista para otro error

    }
    
    @ExceptionHandler(EntidadNoEncontradaException.class)
    public String handleEntidadNoEncontrada(EntidadNoEncontradaException ex, Model model) {
    	System.out.println("⛔ Excepción capturada: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(ErrorValidacionDatosException.class)
    public String handleErrorValidacionDatos(ErrorValidacionDatosException ex, Model model) {
    	   System.out.println("⛔ Excepción capturada: " + ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/400";
    }
    
    
    

}

