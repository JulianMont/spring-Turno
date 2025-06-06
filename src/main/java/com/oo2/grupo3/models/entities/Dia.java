package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Dia_abm")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Dia {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia")
    @Setter(AccessLevel.NONE)
    private Integer id;

	@NotNull(message = "La fecha no puede ser nula")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

	@NotNull(message = "Debe tener al menos una hora")
    @OneToMany(mappedBy = "dia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hora> hora;
	
}
