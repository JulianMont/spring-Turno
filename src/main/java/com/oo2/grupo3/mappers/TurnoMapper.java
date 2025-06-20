package com.oo2.grupo3.mappers;

import org.springframework.stereotype.Component;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.*;
import com.oo2.grupo3.models.enums.EstadoTurno;
import com.oo2.grupo3.repositories.*;

@Component
public class TurnoMapper {

    private final IClienteRepository clienteRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IServicioRepository servicioRepository;
    private final IDiaRepository diaRepository;
    private final IHoraRepository horaRepository;

    public TurnoMapper(IClienteRepository clienteRepository,
                       IEmpleadoRepository empleadoRepository,
                       IServicioRepository servicioRepository,
                       IDiaRepository diaRepository,
                       IHoraRepository horaRepository) {
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.servicioRepository = servicioRepository;
        this.diaRepository = diaRepository;
        this.horaRepository = horaRepository;
    }

    public Turno toEntity(TurnoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Empleado empleado = empleadoRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        Servicio servicio = servicioRepository.findById(dto.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Dia dia = diaRepository.findByFecha(dto.getFecha())
                .orElseGet(() -> diaRepository.save(Dia.builder().fecha(dto.getFecha()).build()));

        Hora hora = horaRepository.findByHoraAndDia(dto.getHora(), dia)
                .orElseGet(() -> horaRepository.save(Hora.builder().hora(dto.getHora()).dia(dia).build()));

        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setEmpleado(empleado);
        turno.setServicio(servicio);
        turno.setDia(dia);
        turno.setHora(hora);

        if (dto.getEstado() != null) {
        	turno.setEstado(dto.getEstado());
        } else {
            turno.setEstado(EstadoTurno.EN_PROCESO);
        }

        return turno;
    }

    public Turno toEntityWithAll(TurnoRequestDTO dto, Cliente cliente, Empleado empleado, Servicio servicio, Dia dia, Hora hora) {
        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setEmpleado(empleado);
        turno.setServicio(servicio);
        turno.setDia(dia);
        turno.setHora(hora);

        if (dto.getEstado() != null) {
            turno.setEstado(dto.getEstado());
        } else {
            turno.setEstado(EstadoTurno.EN_PROCESO);
        }

        return turno;
    }
    
    public TurnoResponseDTO toResponse(Turno turno) {
        return TurnoResponseDTO.builder()
                .idTurno(turno.getIdTurno())
                .idCliente(turno.getCliente().getIdPersona())
                .idEmpleado(turno.getEmpleado().getIdPersona())
                .idServicio(turno.getServicio().getId())
                .clienteNombre(turno.getCliente().getNombreCompleto())
                .empleadoNombre(turno.getEmpleado().getNombreCompleto())
                .servicioNombre(turno.getServicio().getNombre())
                .dia(turno.getDia().getFecha())
                .hora(turno.getHora().getHora())
                .estado(turno.getEstado().name()) 
                .build();
    }

    public TurnoRequestDTO toRequest(Turno turno) {
        return TurnoRequestDTO.builder()
                .idCliente(turno.getCliente().getIdPersona())
                .idEmpleado(turno.getEmpleado().getIdPersona())
                .idServicio(turno.getServicio().getId())
                .fecha(turno.getDia().getFecha())
                .hora(turno.getHora().getHora())
                .estado(turno.getEstado())
                .build();
    }
}
