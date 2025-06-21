package com.oo2.grupo3.helpers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.oo2.grupo3.helpers.exceptions.ClienteRepetidoException;
import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.helpers.exceptions.HorarioNoDisponibleException;
import com.oo2.grupo3.helpers.exceptions.TurnoOcupadoException;
import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(HorarioNoDisponibleException.class)
    public String handleHorarioNoDisponibleException(HorarioNoDisponibleException ex, Model model) {
        model.addAttribute("errorMensaje", ex.getMessage());
        return "error/horarioNoDisponible";  // Otra vista para otro error

    }
    
    @ExceptionHandler(EntidadNoEncontradaException.class)
    public String handleEntidadNoEncontrada(EntidadNoEncontradaException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(ErrorValidacionDatosException.class)
    public String handleErrorValidacionDatos(ErrorValidacionDatosException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/400";
    }
    
    
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
    @ExceptionHandler(ClienteRepetidoException.class)
    public String handleClienteRepetidoException(ClienteRepetidoException ex, Model model) {
        model.addAttribute("errorMensaje", ex.getMessage());
        model.addAttribute("urlVolver", "cliente/form"); // o cambiá esta ruta si querés que vuelva a otro lado 

        return "error/clienteRepetido";
    }

   


}

