package com.oo2.grupo3.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ubicacion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ubicacion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    @Setter(AccessLevel.NONE)
    private Integer id;
	
	
	 @NotBlank(message = "La direccion no puede estar vacia")
	    @Column(name = "direccion", nullable = false)
	    private String direccion;
	 
	 @NotBlank(message = "La ciudad no puede ser vacia")
	    @Column(name = "ciudad", nullable = false)
	    private String ciudad;


}
