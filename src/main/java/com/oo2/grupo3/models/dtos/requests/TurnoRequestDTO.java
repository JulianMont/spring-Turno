package com.oo2.grupo3.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El ID del empleado es obligatorio")
    private Integer idEmpleado;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Integer idServicio;

    @NotNull(message = "El ID del día es obligatorio")
    private Integer idDia;

    @NotNull(message = "El ID de la hora es obligatorio")
    private Integer idHora;
}
