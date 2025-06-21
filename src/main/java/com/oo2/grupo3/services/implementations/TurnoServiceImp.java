package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.exceptions.TurnoOcupadoException;
import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.*;
import com.oo2.grupo3.models.enums.DiaSemana;
import com.oo2.grupo3.models.enums.EstadoTurno;
import com.oo2.grupo3.repositories.*;
import com.oo2.grupo3.services.interfaces.IDiaService;
import com.oo2.grupo3.services.interfaces.ITurnoService;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
    public void cancelarTurno(Integer idTurno) {
        Turno turno = turnoRepository.findById(idTurno)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setEstado(EstadoTurno.CANCELADO);
        turnoRepository.save(turno);
    }

    @Override
    public void actualizarFechaYHora(Integer id, LocalDate nuevaFecha, LocalTime nuevaHora) {
        System.out.println("Actualizando turno ID: " + id);

        Turno turno = findById(id);

        Dia dia = diaService.findByFecha(nuevaFecha)
                .orElseGet(() -> diaService.save(Dia.builder().fecha(nuevaFecha).build()));

        Hora hora = horaRepository.findByHoraAndDia(nuevaHora, dia)
                .orElseGet(() -> horaRepository.save(Hora.builder().hora(nuevaHora).dia(dia).build()));

        if (!empleadoTrabajaEseDiaYHora(turno.getEmpleado(), dia, hora)) {
            throw new RuntimeException("El empleado no trabaja en ese día y horario.");
        }

        System.out.println("Validando disponibilidad del cliente y el empleado...");
        validarDisponibilidadClienteYEmpleado(turno.getCliente(), turno.getEmpleado(), dia, hora, id);

        turno.setDia(dia);
        turno.setHora(hora);

        turnoRepository.save(turno);
        System.out.println("Turno actualizado con éxito.");
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

        LocalTime horaTurno = turnoRequestDTO.getHora();
        int hora = horaTurno.getHour();
        int minutos = horaTurno.getMinute();

        if (hora < 8 || hora > 20 || (hora == 20 && minutos > 0)) {
            throw new RuntimeException("La hora del turno debe estar entre 08:00 y 20:00");
        }
        if (minutos != 0 && minutos != 30) {
            throw new RuntimeException("Los turnos solo pueden comenzar en minutos 00 o 30");
        }

        Dia dia = diaService.findByFecha(turnoRequestDTO.getFecha())
                .orElseThrow(() -> new RuntimeException("Día no encontrado con fecha: " + turnoRequestDTO.getFecha()));

        List<Hora> horas = horaRepository.findAllByHora(turnoRequestDTO.getHora());
        if (horas.isEmpty()) {
            throw new RuntimeException("Hora no encontrada: " + turnoRequestDTO.getHora());
        }
        Hora horaEntidad = horas.get(0);

        if (!empleadoTrabajaEseDiaYHora(empleado, dia, horaEntidad)) {
            throw new RuntimeException("El empleado no trabaja en ese día y horario.");
        }

        validarDisponibilidadClienteYEmpleado(cliente, empleado, dia, horaEntidad);

        Turno turno = Turno.builder()
                .cliente(cliente)
                .empleado(empleado)
                .dia(dia)
                .hora(horaEntidad)
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
        Dia dia = turno.getDia();
        Hora hora = turno.getHora();
        Empleado empleado = turno.getEmpleado();

        // Usamos método para convertir el DayOfWeek a DiaSemana correcto
        DiaSemana diaSemana = convertirDayOfWeekADiaSemana(dia.getFecha().getDayOfWeek());
        LocalTime horaTurno = hora.getHora();

        boolean trabaja = empleado.getHorariosLaborales().stream()
            .anyMatch(hl -> hl.getDiaSemana() == diaSemana
                    && !horaTurno.isBefore(hl.getHoraInicio())
                    && !horaTurno.isAfter(hl.getHoraFin()));

        if (!trabaja) {
            throw new RuntimeException("El empleado no trabaja en el día y hora especificados.");
        }

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
                    nuevaHora.setDia(dia);
                    return horaRepository.save(nuevaHora);
                });

        if (!empleadoTrabajaEseDiaYHora(empleado, dia, horaDia)) {
            throw new RuntimeException("El empleado no trabaja en ese día y horario.");
        }

        validarDisponibilidadClienteYEmpleado(cliente, empleado, dia, horaDia);

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
        Hora horaEntidad = horas.get(0);

        validarDisponibilidadClienteYEmpleado(cliente, empleado, dia, horaEntidad);

        Turno nuevoTurno = new Turno();
        nuevoTurno.setCliente(cliente);
        nuevoTurno.setEmpleado(empleado);
        nuevoTurno.setServicio(servicio);
        nuevoTurno.setDia(dia);
        nuevoTurno.setHora(horaEntidad);
        nuevoTurno.setEstado(EstadoTurno.EN_PROCESO);

        return turnoRepository.save(nuevoTurno);
    }

    @Override
    public void validarDisponibilidadClienteYEmpleado(Cliente cliente, Empleado empleado, Dia dia, Hora hora) {
        if (turnoRepository.existsByEmpleadoAndDiaAndHora(empleado, dia, hora)) {
            throw new TurnoOcupadoException("El empleado ya tiene un turno asignado en ese día y hora.");
        }
        if (turnoRepository.existsByClienteAndDiaAndHora(cliente, dia, hora)) {
            throw new TurnoOcupadoException("El cliente ya tiene un turno asignado en ese día y hora.");
        }
    }

    @Override
    public void validarDisponibilidadClienteYEmpleado(Cliente cliente, Empleado empleado, Dia dia, Hora hora, Integer idTurnoActual) {
        if (turnoRepository.existsByEmpleadoAndDiaAndHoraAndIdTurnoNot(empleado, dia, hora, idTurnoActual)) {
            throw new TurnoOcupadoException("El empleado ya tiene un turno asignado en ese día y hora.");
        }
        if (turnoRepository.existsByClienteAndDiaAndHoraAndIdTurnoNot(cliente, dia, hora, idTurnoActual)) {
            throw new TurnoOcupadoException("El cliente ya tiene un turno asignado en ese día y hora.");
        }
    }

    @Override
    public boolean empleadoTrabajaEseDiaYHora(Empleado empleado, Dia dia, Hora hora) {
        if (empleado.getHorariosLaborales() == null) return false;

        DiaSemana diaSemana = convertirDayOfWeekADiaSemana(dia.getFecha().getDayOfWeek());
        LocalTime horaTurno = hora.getHora();

        return empleado.getHorariosLaborales().stream()
                .anyMatch(horario ->
                        horario.getDiaSemana() == diaSemana &&
                                !horaTurno.isBefore(horario.getHoraInicio()) &&
                                !horaTurno.isAfter(horario.getHoraFin())
                );
    }

    private DiaSemana convertirDayOfWeekADiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }

}
