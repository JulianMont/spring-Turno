package com.oo2.grupo3.mappers;

import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.repositories.IDiaRepository;
import com.oo2.grupo3.repositories.IHoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapper {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IEmpleadoRepository empleadoRepository;

    @Autowired
    private IServicioRepository servicioRepository;

    @Autowired
    private IDiaRepository diaRepository;

    @Autowired
    private IHoraRepository horaRepository;

    public static TurnoResponseDTO toResponse(Turno turno) {
        TurnoResponseDTO dto = new TurnoResponseDTO();
        dto.setIdTurno(turno.getIdTurno());
        dto.setClienteNombre(turno.getCliente().getNombreCompleto());
        dto.setEmpleadoNombre(turno.getEmpleado().getNombreCompleto());
        dto.setServicioNombre(turno.getServicio().getNombre());
        dto.setDia(turno.getDia());
        dto.setHora(turno.getHora());

        return dto;
    }

    public Turno toEntity(TurnoRequestDTO dto) {
        Turno turno = new Turno();

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));
        Empleado empleado = empleadoRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));
        Servicio servicio = servicioRepository.findById(dto.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + dto.getIdServicio()));
        Dia dia = diaRepository.findById(dto.getIdDia())
                .orElseThrow(() -> new RuntimeException("Día no encontrado con ID: " + dto.getIdDia()));
        Hora hora = horaRepository.findById(dto.getIdHora())
                .orElseThrow(() -> new RuntimeException("Hora no encontrada con ID: " + dto.getIdHora()));

        turno.setCliente(cliente);
        turno.setEmpleado(empleado);
        turno.setServicio(servicio);
        turno.setDia(dia);
        turno.setHora(hora);

        return turno;
    }
}
