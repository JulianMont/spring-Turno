package com.oo2.grupo3.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AusenciaEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long idAusenciaEmpleado;

    @NotNull(message = "La fecha de ausencia es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El motivo no puede estar vacío")
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "empleado_id") 
    private Empleado empleado;
}
