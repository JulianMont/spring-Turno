package com.oo2.grupo3.models.entities;


import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_persona") // opcional, para diferenciar subclases
@Table(name = "persona")


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@NotBlank(message = "La Persona debe tener un nombre.")
	private String nombre;

	
	@NotBlank(message = "La Persona debe tener un apellido.")
	private String apellido;
	
	public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
