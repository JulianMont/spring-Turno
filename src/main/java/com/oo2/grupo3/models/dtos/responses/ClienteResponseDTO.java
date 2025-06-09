package com.oo2.grupo3.models.dtos.responses;

import java.util.List;

import com.oo2.grupo3.models.entities.Turno;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder

public class ClienteResponseDTO extends PersonaResponseDTO {

	List<Turno> turnosSolicitados;
	
}
