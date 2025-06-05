
package com.oo2.grupo3.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "servicios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Servicio {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    @Setter(AccessLevel.NONE)
    private Integer id;

	@NotBlank(message = "Tiene qe haber al menos 1 servicio")
	private String nombre;
	
	@OneToMany(mappedBy = "servicio")
	private List<Turno> turnos;


	
	
	
}

