package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.*;
import com.oo2.grupo3.repositories.*;
import com.oo2.grupo3.services.interfaces.IDiaService;
import com.oo2.grupo3.services.interfaces.ITurnoService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurnoServiceImp implements ITurnoService {

    private final ITurnoRepository turnoRepository;
    private final IClienteRepository clienteRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IHoraRepository horaRepository;
    private final IServicioRepository servicioRepository;
    private final TurnoMapper turnoMapper;
    private final IDiaService diaService;
    private final IDiaRepository diaRepository;

    public TurnoServiceImp(ITurnoRepository turnoRepository,
                           IClienteRepository clienteRepository,
                           IEmpleadoRepository empleadoRepository,
                           IDiaService diaService,
                           IDiaRepository diaRepository,
                           IHoraRepository horaRepository,
                           IServicioRepository servicioRepository,
                           TurnoMapper turnoMapper) {
        this.turnoRepository = turnoRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.diaService = diaService;
        this.diaRepository = diaRepository;
        this.horaRepository = horaRepository;
        this.servicioRepository = servicioRepository;
        this.turnoMapper = turnoMapper;
    }

    @Override
    public Page<TurnoResponseDTO> findAll(Pageable pageable) {
        return turnoRepository.findAll(pageable).map(this::convertToDTO);
    }

    private TurnoResponseDTO convertToDTO(Turno turno) {
        return TurnoResponseDTO.builder()
                .clienteNombre(turno.getCliente().getNombre())
                .empleadoNombre(turno.getEmpleado().getNombre())
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

        Dia dia = diaService.findByFecha(turnoRequestDTO.getFecha())
                .orElseThrow(() -> new RuntimeException("Día no encontrado con fecha: " + turnoRequestDTO.getFecha()));

        List<Hora> horas = horaRepository.findAllByHora(turnoRequestDTO.getHora());
        if (horas.isEmpty()) {
            throw new RuntimeException("Hora no encontrada: " + turnoRequestDTO.getHora());
        }
        Hora hora = horas.get(0);

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
        return turnoRepository.findAll().stream()
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

        // Validación hora
        LocalTime horaTurno = requestDTO.getHora();
        int hora = horaTurno.getHour();
        int minutos = horaTurno.getMinute();

        if (hora < 8 || hora > 20 || (hora == 20 && minutos > 0)) {
            throw new RuntimeException("La hora del turno debe estar entre 08:00 y 20:00");
        }
        if (minutos != 0 && minutos != 30) {
            throw new RuntimeException("Los turnos solo pueden comenzar en minutos 00 o 30");
        }

        Dia dia = diaRepository.findAllByFecha(requestDTO.getFecha())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Dia nuevoDia = new Dia();
                    nuevoDia.setFecha(requestDTO.getFecha());
                    return diaRepository.save(nuevoDia);
                });

        Hora horaDia = horaRepository.findAllByHora(requestDTO.getHora())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Hora nuevaHora = new Hora();
                    nuevaHora.setHora(requestDTO.getHora());
                    nuevaHora.setDia(dia);  // <- importante asignar el día aquí
                    return horaRepository.save(nuevaHora);
                });

        validarDisponibilidadEmpleado(empleado, dia, horaDia);

        Turno turno = turnoMapper.toEntityWithAll(requestDTO, cliente, empleado, servicio, dia, horaDia);
        return turnoRepository.save(turno);
    }




    @Override
    public void deleteById(Integer id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado."));
        turnoRepository.delete(turno);
    }

    @Override
    public Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, LocalDate fecha, LocalTime hora) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Dia dia = diaService.findByFecha(fecha)
                .orElseThrow(() -> new RuntimeException("Día no encontrado para la fecha: " + fecha));

        List<Hora> horas = horaRepository.findAllByHora(hora);
        if (horas.isEmpty()) {
            throw new RuntimeException("Hora no encontrada");
        }
        Hora horaEntidad = horas.get(0); // o alguna lógica para decidir

        validarDisponibilidadEmpleado(empleado, dia, horaEntidad);

        Turno nuevoTurno = new Turno();
        nuevoTurno.setCliente(cliente);
        nuevoTurno.setEmpleado(empleado);
        nuevoTurno.setServicio(servicio);
        nuevoTurno.setDia(dia);
        nuevoTurno.setHora(horaEntidad);

        return turnoRepository.save(nuevoTurno);
    }

    private void validarDisponibilidadEmpleado(Empleado empleado, Dia dia, Hora horaEntidad) {
		// TODO Auto-generated method stub
		
	}

	public void validarDisponibilidadClienteYEmpleado(Cliente cliente, Empleado empleado, Dia dia, Hora hora) {
        if (turnoRepository.existsByEmpleadoAndDiaAndHora(empleado, dia, hora)) {
            throw new RuntimeException("El empleado ya tiene un turno asignado en ese día y hora.");
        }
        if (turnoRepository.existsByClienteAndDiaAndHora(cliente, dia, hora)) {
            throw new RuntimeException("El cliente ya tiene un turno asignado en ese día y hora.");
        }
    }
}

/*@Service
public class TurnoServiceImpl implements ITurnoService {

    @Autowired
    private ITurnoRepository turnoRepository;

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

    @Autowired
    private TurnoMapper turnoMapper;  

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

    // IMPLEMENTACIÓN DEL MÉTODO OBLIGATORIO DE LA INTERFAZ PARA GUARDAR CON DTO
    @Override
    public Turno save(TurnoRequestDTO requestDTO) {
        Turno turno = turnoMapper.toEntity(requestDTO);
        return turnoRepository.save(turno);
    }

    @Override
    public void deleteById(Integer id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado."));
        turnoRepository.delete(turno);
    }

	@Override
	public List<TurnoResponseDTO> obtenerTodosLosTurnos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Turno> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TurnoResponseDTO> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, LocalDate fecha,
			LocalTime hora) {
		// TODO Auto-generated method stub
		return null;
	}

    //@Override
    /*public Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, Integer idDia, Integer idHora) {

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

        Turno nuevoTurno = new Turno();
        nuevoTurno.setCliente(cliente);
        nuevoTurno.setEmpleado(empleado);
        nuevoTurno.setServicio(servicio);
        nuevoTurno.setDia(dia);
        nuevoTurno.setHora(hora);

        return turnoRepository.save(nuevoTurno);
    }*/

