package com.oo2.grupo3.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Notificacion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Notificacion {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    @Setter(AccessLevel.NONE)
    private Integer id;
	

	@NotNull(message = "La Persona no puede ser vacia")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", nullable = false)
	private Persona persona;

	@NotBlank(message = "El tipo no puede estar vacio")
	@Column(name = "tipo", nullable = false)
	private String tipo;
	

	@NotBlank(message = "El mensaje no puede estar vacio")
	@Column(name = "mensaje", nullable = false)
	private String mensaje;
	
	@NotNull(message = "La fecha de envío no puede ser nula")
	@Column(name = "fecha_envio", nullable = false)
	private LocalDateTime fechaEnvio;

	

}
