
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


import com.oo2.grupo3.repositories.TurnoRepository;

import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IDiaRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IHoraRepository;


import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.repositories.ITurnoRepository;
import com.oo2.grupo3.services.interfaces.ITurnoService;

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

    /*@Override
    public TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO) {

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

        Turno nuevoTurno = new Turno();
        nuevoTurno.setCliente(cliente);
        nuevoTurno.setEmpleado(empleado);
        nuevoTurno.setServicio(servicio);
        nuevoTurno.setDia(dia);
        nuevoTurno.setHora(hora);

        return turnoRepository.save(nuevoTurno);
    }

    // Podés agregar más métodos para eliminar, actualizar, etc.

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
}
