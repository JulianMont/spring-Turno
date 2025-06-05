package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;

import com.oo2.grupo3.repositories.ITurnoRepository;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.repositories.IDiaRepository;
import com.oo2.grupo3.repositories.IHoraRepository;

import com.oo2.grupo3.services.interfaces.ITurnoService;
import com.oo2.grupo3.mappers.TurnoMapper; // 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
}
