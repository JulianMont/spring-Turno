package com.oo2.grupo3.models.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.DiscriminatorValue;

import jakarta.persistence.Column;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("EMPLEADO")
@NoArgsConstructor
@AllArgsConstructor

public class Empleado extends Persona{

    @NotBlank(message = "El legajo es obligatorio")
    @Column(unique = true, nullable = true)
    private String legajo;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<HorarioLaboral> horariosLaborales = new ArrayList<>();

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<AusenciaEmpleado> diasAusentes = new ArrayList<>();
}
