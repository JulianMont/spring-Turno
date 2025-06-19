package com.oo2.grupo3.mappers;

import org.springframework.stereotype.Component;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Hora;
import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Turno;

import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IDiaRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IHoraRepository;
import com.oo2.grupo3.repositories.IServicioRepository;

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

        return turno;
    }

    public Turno toEntityWithAll(TurnoRequestDTO dto, Cliente cliente, Empleado empleado, Servicio servicio, Dia dia, Hora hora) {
        Turno turno = new Turno();
        turno.setCliente(cliente);
        turno.setEmpleado(empleado);
        turno.setServicio(servicio);
        turno.setDia(dia);
        turno.setHora(hora);
        return turno;
    }

    public TurnoResponseDTO toResponse(Turno turno) {
        return TurnoResponseDTO.builder()
        		.idTurno(turno.getIdTurno())
        		.clienteNombre(turno.getCliente().getNombre())  // uso de getNombreCompleto()
                .empleadoNombre(turno.getEmpleado().getNombre()) // uso de getNombreCompleto()
                .servicioNombre(turno.getServicio().getNombre())
                .dia(turno.getDia().getFecha()) 
                .hora(turno.getHora().getHora())
                .build();
    }
}
