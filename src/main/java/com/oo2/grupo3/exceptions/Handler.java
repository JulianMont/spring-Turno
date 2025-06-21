package com.oo2.grupo3.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class Handler {

	@ExceptionHandler(TurnoOcupadoException.class)
	public String handleTurnoOcupadoException(TurnoOcupadoException ex, Model model, HttpServletRequest request) {
	    model.addAttribute("errorMensaje", ex.getMessage());

	    String referer = request.getHeader("referer");
	    if (referer != null && referer.contains("/editar/")) {
	        model.addAttribute("urlVolver", referer); 
	    } else {
	        model.addAttribute("urlVolver", "/turnos/GenerarTurno"); 
	    }

	    return "error/turnoOcupado";
	}
	
	
	

    @ExceptionHandler(HorarioNoDisponibleException.class)
    public String handleHorarioNoDisponibleException(HorarioNoDisponibleException ex, Model model) {
        model.addAttribute("errorMensaje", ex.getMessage());
        return "error/horarioNoDisponible";  

    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMensaje", "Ha ocurrido un error inesperado: " + ex.getMessage());

        return "error/errorGeneral";  
    }
}

