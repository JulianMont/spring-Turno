

package com.oo2.grupo3.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPersona;
	
	@NotBlank(message = "La Persona debe tener un nombre.")
	@Size(max = 20)
	private String nombre;

	@NotBlank(message = "La Persona debe tener un apellido.")
	@Size(max = 20)
	private String apellido;
	
    @NotNull
    @Min(1000000)
    @Column(unique = true, nullable = false)
    private int dni;
    
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserEntity user;

    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
	
	
	
}
