
package com.oo2.grupo3.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Servicio")
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
	
	
	 @NotBlank(message = "El nombre no puede estar vacio")
	    @Column(name = "nombre", nullable = false)
	    private String nombre;
	 
	 @NotNull(message = "La ubicacion no puede ser vacia")
	    @OneToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "id_ubicacion", nullable = false)
	    private Ubicacion ubicacion;

}

