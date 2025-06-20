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
@Table(name = "Contacto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


public class Contacto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    @Setter(AccessLevel.NONE)
    private Integer id;
	
	
	 @NotBlank(message = "El email no puede estar vacio")
	    @Column(name = "email", nullable = false)
	    private String email;
	 
	 @NotBlank(message = "El telefono no puede estar vacio")
	    @Column(name = "telefono", nullable = false)
	    private String telefono;

}
