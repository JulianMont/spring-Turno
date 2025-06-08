package com.oo2.grupo3.models.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.oo2.grupo3.models.enums.TipoPersona;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "empleado")
@DiscriminatorValue("EMPLEADO")
public class Empleado extends Persona{

    @NotBlank(message = "El legajo es obligatorio")
    @Column(unique = true, nullable = false)
    private String legajo;

    @NotNull(message = "La especialidad es obligatoria")
    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HorarioLaboral> horariosLaborales = new HashSet<>();

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AusenciaEmpleado> diasAusentes;
    
    public Empleado() {
        super();
        this.setTipoPersona(TipoPersona.EMPLEADO);
    }
}