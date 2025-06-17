package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Dia")
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
	@Column(name = "fecha", nullable = false, unique = true)
	private LocalDate fecha;

	@NotNull(message = "Debe tener al menos una hora")
	@OneToMany(mappedBy = "dia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Hora> hora;
	
}
