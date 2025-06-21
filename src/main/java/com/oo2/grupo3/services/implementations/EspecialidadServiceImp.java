package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.models.dtos.requests.EspecialidadRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.models.entities.Especialidad;
import com.oo2.grupo3.repositories.IEspecialidadRepository;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;

@Service
public class EspecialidadServiceImp implements IEspecialidadService {
	
    private final IEspecialidadRepository especialidadRepository;
    private final ModelMapper modelMapper;

    public EspecialidadServiceImp(IEspecialidadRepository empleadoRepository, ModelMapper modelMapper) {
        this.especialidadRepository = empleadoRepository;
        this.modelMapper = modelMapper;
    }

	@Override
	public List<EspecialidadResponseDTO> traerEspecialidades() {
		return especialidadRepository.findAll()
				.stream()
				.map(especialidad -> modelMapper.map(especialidad, EspecialidadResponseDTO.class))
				.collect(Collectors.toList());
				
	}
	
	@Override
	public EspecialidadResponseDTO findById(Long id) {
	    Especialidad especialidad = especialidadRepository.findById(id)
	    		.orElseThrow(() -> new EntidadNoEncontradaException("Especialidad no encontrada con ID: " + id));

	    return modelMapper.map(especialidad, EspecialidadResponseDTO.class);
	}
    

	@Override
	public EspecialidadResponseDTO crearEspecialidad(EspecialidadRequestDTO dtoEspecialidad) {
		if(especialidadRepository.existsByNombreIgnoreCase(dtoEspecialidad.getNombre())) {
			throw new ErrorValidacionDatosException("Ya existe una especialidad con ese nombre");
		}
		
		Especialidad especialidad = modelMapper.map(dtoEspecialidad, Especialidad.class);
		
		return modelMapper.map(especialidadRepository.save(especialidad) , EspecialidadResponseDTO.class);
	}

	@Override
	public EspecialidadResponseDTO editarEspecialidad(Long id, EspecialidadRequestDTO dtoEspecialidad) {
		Especialidad especialidad = especialidadRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("La Especialidad no se encontro"));
		
		especialidad.setNombre(dtoEspecialidad.getNombre());
		
		return modelMapper.map(especialidadRepository.save(especialidad), EspecialidadResponseDTO.class);
	}

	@Override
	public boolean borrarEspecialidad(Long id) {
		
		Especialidad  especialidad = especialidadRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("La Especialidad no se encontro"));
		especialidadRepository.delete(especialidad);
		
		return true;
	}

	
    
 
}
