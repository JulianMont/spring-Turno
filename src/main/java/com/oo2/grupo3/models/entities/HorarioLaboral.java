package com.oo2.grupo3.models.entities;

import java.time.LocalTime;

import com.oo2.grupo3.models.enums.DiaSemana;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class HorarioLaboral {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Setter(AccessLevel.NONE)
	 private Integer idHorarioLaboral;
	 
	 @Enumerated(EnumType.STRING)
	 private DiaSemana diaSemana;
	 
	 private LocalTime horaInicio;
	 private LocalTime horaFin;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "empleado_legajo")
	 private Empleado empleado;
	  

}
