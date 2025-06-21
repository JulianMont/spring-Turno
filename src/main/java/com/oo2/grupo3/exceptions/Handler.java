package com.oo2.grupo3.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class Handler {

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

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMensaje", "Ha ocurrido un error inesperado: " + ex.getMessage());
        return "error/errorGeneral";  // Vista genérica para errores no contemplados
    }
}
