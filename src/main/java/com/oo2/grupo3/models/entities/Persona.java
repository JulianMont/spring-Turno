package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Persona")
    @Setter(AccessLevel.NONE)
    private Integer id;
	

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(name = "apellido", nullable = false)
    private String apellido;

    public String getNombreCompleto() {
        return apellido + " " + nombre;
        
    }
}