package com.oo2.grupo3.models.entities;

import com.oo2.grupo3.models.enums.EstadoTurno;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "turno",
uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cliente_id", "id_dia", "id_hora"}),   // Un cliente no puede tener dos turnos en el mismo día y hora
    @UniqueConstraint(columnNames = {"id_empleado", "id_dia", "id_hora"})  // Un empleado no puede tener dos turnos en el mismo día y hora
}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Integer idTurno;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El empleado es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;
    
    @NotNull(message = "El servicio es obligatorio")
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @NotNull(message = "El día es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia", nullable = false)
    private Dia dia;
    
    @NotNull(message = "El día es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hora", nullable = false)
    private Hora hora;
    
    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;
    

}
