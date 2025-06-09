package com.oo2.grupo3.models.dtos.responses;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClienteResponseDTO {

	private int id;
    private String nombre;
    private String apellido;
    
    private List<TurnoResponseDTO> turnosSolicitados;

  
}
