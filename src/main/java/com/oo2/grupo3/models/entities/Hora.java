package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.Objects;


@Entity
@Table(name = "hora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hora")
    private Integer id;

    @NotNull(message = "La hora no puede ser nula")
    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @NotNull(message = "La referencia al día es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia", nullable = false)
    private Dia dia;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hora)) return false;
        Hora other = (Hora) o;
        return Objects.equals(hora, other.hora) &&
               Objects.equals(dia != null ? dia.getId() : null, other.dia != null ? other.dia.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hora, dia != null ? dia.getId() : null);
    }
    
}
