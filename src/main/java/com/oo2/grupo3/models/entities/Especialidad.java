package com.oo2.grupo3.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Especialidad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long idEspecialidad;
	
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(max = 30, message = "El nombre no puede tener más de 30 caracteres")
	@Pattern(regexp = "^[\\p{L} ]+$", message = "El nombre solo puede contener letras y espacios")
	@Column(name = "nombre", nullable = false, length = 30)
	private String nombre;
	

}