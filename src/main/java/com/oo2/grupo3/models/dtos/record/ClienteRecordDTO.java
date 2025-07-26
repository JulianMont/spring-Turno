package com.oo2.grupo3.models.dtos.record;
import java.util.List;

import com.oo2.grupo3.models.dtos.requests.UserRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ClienteRecordDTO(
	   
		
	    String nombre,
	    
	    String apellido,
	    
	    int dni,
	
	    UserRequestDTO user)
	 {
	    public String getNombreCompleto() {
	        return nombre + " " + apellido;
	    }
	}

