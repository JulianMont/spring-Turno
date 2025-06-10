package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.mappers.TurnoMapper;
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
import com.oo2.grupo3.repositories.ITurnoRepository;
import com.oo2.grupo3.services.interfaces.ITurnoService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurnoServiceImp implements ITurnoService {

    private final ITurnoRepository turnoRepository;
    private final IClienteRepository clienteRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IDiaRepository diaRepository;
    private final IHoraRepository horaRepository;
    private final IServicioRepository servicioRepository;
    private final TurnoMapper turnoMapper;

    public TurnoServiceImp(ITurnoRepository turnoRepository,
                           IClienteRepository clienteRepository,
                           IEmpleadoRepository empleadoRepository,
                           IDiaRepository diaRepository,
                           IHoraRepository horaRepository,
                           IServicioRepository servicioRepository,
                           TurnoMapper turnoMapper) {
        this.turnoRepository = turnoRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.diaRepository = diaRepository;
        this.horaRepository = horaRepository;
        this.servicioRepository = servicioRepository;
        this.turnoMapper = turnoMapper;
    }

    @Override
    public Page<TurnoResponseDTO> findAll(Pageable pageable) {
        Page<Turno> turnos = turnoRepository.findAll(pageable);
        return turnos.map(this::convertToDTO);
    }

    private TurnoResponseDTO convertToDTO(Turno turno) {
        return TurnoResponseDTO.builder()
                .clienteNombre(turno.getCliente().getNombreCompleto())
                .empleadoNombre(turno.getEmpleado().getNombreCompleto())
                .servicioNombre(turno.getServicio().getNombre())
                .dia(turno.getDia().getFecha())
                .hora(turno.getHora().getHora())
                .build();
    }

    @Override
    public List<Turno> getAll() {
        return turnoRepository.findAll(); 
    }
    
    

    @Override
    public TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO) {
        Cliente cliente = clienteRepository.findById(turnoRequestDTO.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Empleado empleado = empleadoRepository.findById(turnoRequestDTO.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Servicio servicio = servicioRepository.findById(turnoRequestDTO.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Dia dia = diaRepository.findByFecha(turnoRequestDTO.getFecha())
                .orElseThrow(() -> new RuntimeException("Día no encontrado con fecha: " + turnoRequestDTO.getFecha()));
        Hora hora = horaRepository.findByHora(turnoRequestDTO.getHora())
                .orElseThrow(() -> new RuntimeException("Hora no encontrada: " + turnoRequestDTO.getHora()));

        validarDisponibilidadEmpleado(empleado, dia, hora);

        Turno turno = Turno.builder()
                .cliente(cliente)
                .empleado(empleado)
                .dia(dia)
                .hora(hora)
                .servicio(servicio)
                .build();

        Turno turnoGuardado = turnoRepository.save(turno);
        return turnoMapper.toResponse(turnoGuardado);
    }

    @Override
    public List<TurnoResponseDTO> obtenerTodosLosTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos.stream()
                .map(turnoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    @Override
    public Turno findById(Integer id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado."));
    }

    @Override
    public Turno save(Turno turno) {
        return turnoRepository.save(turno);
    }

    @Override
    public Turno save(TurnoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Empleado empleado = empleadoRepository.findById(requestDTO.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Servicio servicio = servicioRepository.findById(requestDTO.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        Dia dia = diaRepository.findByFecha(requestDTO.getFecha())
                .orElseThrow(() -> new RuntimeException("Día no encontrado con fecha: " + requestDTO.getFecha()));
        Hora hora = horaRepository.findByHora(requestDTO.getHora())
                .orElseThrow(() -> new RuntimeException("Hora no encontrada: " + requestDTO.getHora()));

        validarDisponibilidadEmpleado(empleado, dia, hora);

        Turno turno = turnoMapper.toEntityWithAll(requestDTO, cliente, empleado, servicio, dia, hora);

        return turnoRepository.save(turno);
    }

    @Override
    public void deleteById(Integer id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado."));
        turnoRepository.delete(turno);
    }

    @Override
    public Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, Integer idDia, Integer idHora) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        Dia dia = diaRepository.findById(idDia)
                .orElseThrow(() -> new RuntimeException("Día no encontrado"));
        Hora hora = horaRepository.findById(idHora)
                .orElseThrow(() -> new RuntimeException("Hora no encontrada"));

        validarDisponibilidadEmpleado(empleado, dia, hora);

        Turno nuevoTurno = new Turno();
        nuevoTurno.setCliente(cliente);
        nuevoTurno.setEmpleado(empleado);
        nuevoTurno.setServicio(servicio);
        nuevoTurno.setDia(dia);
        nuevoTurno.setHora(hora);

        return turnoRepository.save(nuevoTurno);
    }

    private void validarDisponibilidadEmpleado(Empleado empleado, Dia dia, Hora hora) {
        boolean existeTurno = turnoRepository.existsByEmpleadoAndDiaAndHora(empleado, dia, hora);
        if (existeTurno) {
            throw new RuntimeException("El empleado ya tiene un turno asignado en ese día y hora.");
        }
    }
}