package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.configurations.mapper.TurnoMapper;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;
import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.repositories.ClienteRepository;
import com.oo2.grupo3.repositories.EmpleadoRepository;
import com.oo2.grupo3.repositories.TurnoRepository;
import com.oo2.grupo3.repositories.DiaRepository;
import com.oo2.grupo3.repositories.HoraRepository;
import com.oo2.grupo3.repositories.ServicioRepository;
import com.oo2.grupo3.services.interfaces.TurnoService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurnoServiceImp implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final DiaRepository diaRepository;
    private final HoraRepository horaRepository;
    private final ServicioRepository servicioRepository;

    public TurnoServiceImp(TurnoRepository turnoRepository, ClienteRepository clienteRepository,
                            EmpleadoRepository empleadoRepository, DiaRepository diaRepository,
                            HoraRepository horaRepository, ServicioRepository servicioRepository) {
        this.turnoRepository = turnoRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.diaRepository = diaRepository;
        this.horaRepository = horaRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO) {
        // Buscar las entidades relacionadas a partir de los IDs del request
        Cliente cliente = clienteRepository.findById(turnoRequestDTO.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Empleado empleado = empleadoRepository.findById(turnoRequestDTO.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Dia dia = diaRepository.findById(turnoRequestDTO.getIdDia())
                .orElseThrow(() -> new RuntimeException("Día no encontrado"));
        Hora hora = horaRepository.findById(turnoRequestDTO.getIdHora())
                .orElseThrow(() -> new RuntimeException("Hora no encontrada"));
        Servicio servicio = servicioRepository.findById(turnoRequestDTO.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Crear la entidad Turno
        Turno turno = Turno.builder()
                .cliente(cliente)
                .empleado(empleado)
                .dia(dia)
                .hora(hora)
                .servicio(servicio)
                .build();

        // Guardar el turno
        Turno turnoGuardado = turnoRepository.save(turno);

        // Mapear a DTO de respuesta y devolver
        return TurnoMapper.toResponseDTO(turnoGuardado);
    }

    @Override
    public List<TurnoResponseDTO> obtenerTodosLosTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos.stream()
                .map(TurnoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Podés agregar más métodos para eliminar, actualizar, etc.
}
