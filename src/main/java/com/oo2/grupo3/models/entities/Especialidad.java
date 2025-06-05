package com.oo2.grupo3.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Especialidad_abm")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Especialidad {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    @Setter(AccessLevel.NONE)
    private Integer id;
	
	@NotBlank(message = "La especialidad debe tener un nombre.")
	String especialidad;
	
	
}
