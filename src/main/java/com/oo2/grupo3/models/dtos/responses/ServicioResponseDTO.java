package com.oo2.grupo3.models.dtos.responses;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponseDTO {

    private Integer id;
    private String nombre;
    private UbicacionResponseDTO ubicacion;

}