package com.oo2.grupo3.models.dtos.responses;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponseDTO {
	
    private Integer idPersona;
    private String nombre;
    private String apellido;
    private int dni;
    private UserResponseDTO user;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
