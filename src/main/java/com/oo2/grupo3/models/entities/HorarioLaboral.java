package com.oo2.grupo3.models.entities;

import java.time.LocalTime;
import java.util.List;

import com.oo2.grupo3.models.enums.DiaSemana;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    // Lista de días de la semana para este horario
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "horario_dias", joinColumns = @JoinColumn(name = "horario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia")
    private List<DiaSemana> diasSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_legajo")
    private Empleado empleado;
}
