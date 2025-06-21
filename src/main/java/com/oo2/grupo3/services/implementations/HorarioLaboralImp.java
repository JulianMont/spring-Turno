package com.oo2.grupo3.services.implementations;

import java.time.Duration;

import java.util.Comparator;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.HorarioLaboralRequestDTO;
import com.oo2.grupo3.models.dtos.responses.HorarioLaboralResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.HorarioLaboral;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IHorarioLaboralRepository;
import com.oo2.grupo3.services.interfaces.IHorarioLaboralService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HorarioLaboralImp implements IHorarioLaboralService  {

    private final IHorarioLaboralRepository horarioLaboralRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final ModelMapper modelMapper;
    
	public HorarioLaboralImp(IHorarioLaboralRepository horarioLaboralRepository, IEmpleadoRepository empleadoRepository,ModelMapper modelMapper) {
		this.horarioLaboralRepository = horarioLaboralRepository;
		this.empleadoRepository = empleadoRepository;
		this.modelMapper = modelMapper;
	}

    @Override
    public List<HorarioLaboralResponseDTO> traerHorariosLaborales(Integer idEmpleado) {
        List<HorarioLaboral> horarios = horarioLaboralRepository.findByEmpleado_IdPersonaOrderByDiaSemanaAscHoraInicioAsc(idEmpleado);
        return horarios.stream()
                .map(h -> modelMapper.map(h, HorarioLaboralResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HorarioLaboralResponseDTO agregarHorario(Integer idEmpleado, HorarioLaboralRequestDTO dto) {
    	
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));
        
        if (dto.getHoraFin().isBefore(dto.getHoraInicio()) || dto.getHoraFin().equals(dto.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        
        long tiempoExistente = empleado.getHorariosLaborales().stream()
                .filter(h -> h.getDiaSemana().equals(dto.getDiaSemana()))
                .mapToLong(h -> Duration.between(h.getHoraInicio(), h.getHoraFin()).toMinutes())
                .sum();
        
        //filtro para que 1 turno o 2 no superen las 8 hrs , las horas extras preguntar si es mejor agregarlas desde una entitie nueva
        long tiempoNuevo = java.time.Duration.between(dto.getHoraInicio(), dto.getHoraFin()).toMinutes();
        // Recordatorio: horas*minutos = tiempo 
        if( (tiempoExistente + tiempoNuevo) > 8*60) {
        	throw new IllegalArgumentException("El total de horas trabajadas no puede ser superior a 8 hrs");
        }
        
        //filtro para evitar que se pisen los horarios. ej: 8 a 12 y 10 a 14
        
        boolean superpone = empleado.getHorariosLaborales().stream()
        	    .filter(h -> h.getDiaSemana().equals(dto.getDiaSemana()))
        	    .anyMatch(h -> 
        	        (dto.getHoraInicio().isBefore(h.getHoraFin()) && dto.getHoraFin().isAfter(h.getHoraInicio()))
        	    );

        if (superpone) {
        	
        	throw new IllegalArgumentException("El horario que ingresaste se superpone con otro existente");
        }
        
        //filtro para tener 2 turnos por dia
        long cantHorarioXDia = empleado.getHorariosLaborales().stream().filter(h -> h.getDiaSemana().equals(dto.getDiaSemana())).count();
        if(cantHorarioXDia >= 2) {
        	throw new IllegalArgumentException("No se pueden registrar más de 2 horarios en el mismo día.");
        }
        
        
        HorarioLaboral horario = modelMapper.map(dto, HorarioLaboral.class);
        horario.setEmpleado(empleado);

        HorarioLaboral guardado = horarioLaboralRepository.save(horario);
        return modelMapper.map(guardado, HorarioLaboralResponseDTO.class);
    }

    @Override
    public HorarioLaboralResponseDTO editarHorario(Integer idEmpleado, Integer idHorarioLaboral, HorarioLaboralRequestDTO dto) {
        HorarioLaboral horario = horarioLaboralRepository.findById(idHorarioLaboral)
                .orElseThrow(() -> new EntityNotFoundException("Horario laboral no encontrado"));

        if (!horario.getEmpleado().getIdPersona().equals(idEmpleado)) {
            throw new IllegalArgumentException("El horario no pertenece al empleado especificado");
        }
        
        if (dto.getHoraFin().isBefore(dto.getHoraInicio()) || dto.getHoraFin().equals(dto.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }

        //filtro para tener 2 turnos por dia
        long cantHorarioXDia = horario.getEmpleado().getHorariosLaborales().stream()
        	    .filter(h -> h.getDiaSemana().equals(dto.getDiaSemana()))
        	    .filter(h -> !h.getIdHorarioLaboral().equals(dto.getIdHorarioLaboral())) // excluir el que estoy editando
        	    .count();
        if(cantHorarioXDia >= 2) {
        	throw new IllegalArgumentException("No se pueden registrar más de 2 horarios en el mismo día.");
        }
        
        long tiempoExistente = horario.getEmpleado().getHorariosLaborales().stream()
                .filter(h -> h.getDiaSemana().equals(dto.getDiaSemana()))
                .mapToLong(h -> Duration.between(h.getHoraInicio(), h.getHoraFin()).toMinutes())
                .sum();
        
        //filtro para que 1 turno o 2 no superen las 8 hrs , las horas extras preguntar si es mejor agregarlas desde una entitie nueva
        long tiempoNuevo = java.time.Duration.between(dto.getHoraInicio(), dto.getHoraFin()).toMinutes();
        // Recordatorio: horas*minutos = tiempo 
        if( (tiempoExistente + tiempoNuevo) > 8*60) {
        	throw new IllegalArgumentException("El total de horas trabajadas no puede ser superior a 8 hrs");
        }
        
        //filtro para evitar que se pisen los horarios. ej: 8 a 12 y 10 a 14
        
        boolean superpone = horario.getEmpleado().getHorariosLaborales().stream()
        	    .filter(h -> h.getDiaSemana().equals(dto.getDiaSemana()))
        	    .anyMatch(h -> 
        	        (dto.getHoraInicio().isBefore(h.getHoraFin()) && dto.getHoraFin().isAfter(h.getHoraInicio()))
        	    );

        if (superpone) {
        	
        	throw new IllegalArgumentException("El horario que ingresaste se superpone con otro existente");
        }
        
        
        
        
        
        
        

        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());

        HorarioLaboral actualizado = horarioLaboralRepository.save(horario);
        return modelMapper.map(actualizado, HorarioLaboralResponseDTO.class);
    }

    @Override
    public boolean eliminarHorario(Integer idEmpleado, Integer idHorarioLaboral) {
        HorarioLaboral horario = horarioLaboralRepository.findById(idHorarioLaboral)
                .orElseThrow(() -> new EntityNotFoundException("Horario laboral no encontrado"));
        if (!horario.getEmpleado().getIdPersona().equals(idEmpleado)) {
            throw new IllegalArgumentException("El horario no pertenece al empleado especificado");
        }

        //TODO: Agregar tryCatch
        
        horarioLaboralRepository.delete(horario);
        return true;
    }

	@Override
	public HorarioLaboralResponseDTO findbyId(Integer id) {
		HorarioLaboral horario = horarioLaboralRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario laboral con id " + id + " no existe"));
		return modelMapper.map(horario, HorarioLaboralResponseDTO.class);
	}
	
	@Override
	public List<HorarioLaboralResponseDTO> obtenerHorariosDelEmpleado(Integer idEmpleado) {
	    Empleado empleado = empleadoRepository.findById(idEmpleado)
	            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

	    return empleado.getHorariosLaborales().stream()
	            .map(horario -> HorarioLaboralResponseDTO.builder()
	                    .idHorarioLaboral(horario.getIdHorarioLaboral())
	                    .diaSemana(horario.getDiaSemana())
	                    .horaInicio(horario.getHoraInicio())
	                    .horaFin(horario.getHoraFin())
	                    .build())
	            .collect(Collectors.toList());
	}
	
}
