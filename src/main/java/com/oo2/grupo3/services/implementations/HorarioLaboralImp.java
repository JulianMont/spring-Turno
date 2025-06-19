package com.oo2.grupo3.services.implementations;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.HorarioLaboralRequestDTO;
import com.oo2.grupo3.models.dtos.responses.HorarioLaboralResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.HorarioLaboral;
import com.oo2.grupo3.models.enums.DiaSemana;
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
	    List<HorarioLaboral> horarios = horarioLaboralRepository.findByEmpleado_IdPersona(idEmpleado);

	    horarios.sort(Comparator
	        .comparing(
	        		(HorarioLaboral h) -> {
	                if (h.getDiasSemana() != null && !h.getDiasSemana().isEmpty()) {
	                    return h.getDiasSemana().get(0);
	                }
	                return null;
	            },
	            Comparator.nullsLast(Comparator.naturalOrder())
	        )
	        .thenComparing(
	            HorarioLaboral::getHoraInicio,
	            Comparator.nullsLast(Comparator.naturalOrder())
	        )
	    );

	    return horarios.stream()
	            .map(h -> modelMapper.map(h, HorarioLaboralResponseDTO.class))
	            .collect(Collectors.toList());
	}
    @Override
    public HorarioLaboralResponseDTO agregarHorario(Integer idEmpleado, HorarioLaboralRequestDTO dto) {
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));

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

        horario.setDiasSemana(dto.getDiasSemana());
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
}
