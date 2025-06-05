


package com.oo2.grupo3.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("EMPLEADO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Empleado extends Persona {

	
	@NotBlank(message = "El empleado debe tener un legajo debe tener un nombre.")
	private String legajo;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;
}

