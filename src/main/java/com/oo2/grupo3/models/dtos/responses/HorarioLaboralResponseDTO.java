package com.oo2.grupo3.models.dtos.responses;

import java.time.LocalTime;
import java.util.List;

import com.oo2.grupo3.models.enums.DiaSemana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class HorarioLaboralResponseDTO {
  
    private Integer idHorarioLaboral;
    private List<DiaSemana> diasSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public List<String> getDiasSemanaEsp() {
        return diasSemana.stream()
            .map(dia -> {
                switch (dia) {
                    case LUNES: return "LUNES";
                    case MARTES: return "MARTES";
                    case MIERCOLES: return "MIÉRCOLES";
                    case JUEVES: return "JUEVES";
                    case VIERNES: return "VIERNES";
                    case SABADO: return "SÁBADO";
                    case DOMINGO: return "DOMINGO";
                    default: return dia.toString();
                }
            }).toList();
    }
}
