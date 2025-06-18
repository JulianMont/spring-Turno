package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.AusenciaEmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.AusenciaEmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.AusenciaEmpleado;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.repositories.IAusenciaEmpleadoRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.services.interfaces.IAusenciaEmpleadoService;

import jakarta.persistence.EntityNotFoundException;


@Service
public class AusenciaEmpleadoServiceImp implements IAusenciaEmpleadoService {
	
	private final IAusenciaEmpleadoRepository ausenciaEmpleadoRepository;
	private final IEmpleadoRepository empleadoRepository;
	
	private final ModelMapper modelMapper;
	
	public AusenciaEmpleadoServiceImp(IAusenciaEmpleadoRepository ausenciaEmpleadoRepository, IEmpleadoRepository empleadoRepository,ModelMapper modelMapper) {
		this.ausenciaEmpleadoRepository = ausenciaEmpleadoRepository;
		this.empleadoRepository = empleadoRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<AusenciaEmpleadoResponseDTO> traerAusenciasEmpleado(Integer idEmpleado) {
		return ausenciaEmpleadoRepository.findByEmpleado_IdPersona(idEmpleado)
				.stream()
				.map(ausencia -> modelMapper.map(ausencia, AusenciaEmpleadoResponseDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public AusenciaEmpleadoResponseDTO agregarAusencia(Integer idEmpleado, AusenciaEmpleadoRequestDTO dtoAusencia) {
		
		Empleado empleado = empleadoRepository.findById(idEmpleado)
				.orElseThrow(()-> new EntityNotFoundException("Empleado no encontrado"));
		
		AusenciaEmpleado ausencia = modelMapper.map(dtoAusencia, AusenciaEmpleado.class);
		ausencia.setEmpleado(empleado);
		
		AusenciaEmpleado ausenciaCreada = ausenciaEmpleadoRepository.save(ausencia);
	
		return modelMapper.map(ausenciaCreada, AusenciaEmpleadoResponseDTO.class);
	}

	@Override
	public AusenciaEmpleadoResponseDTO modificarAusencia(Integer idEmpleado, Integer idAusencia,
			AusenciaEmpleadoRequestDTO dto) {
		
		AusenciaEmpleado ausencia = ausenciaEmpleadoRepository.findById(idAusencia)
				.orElseThrow(() -> new EntityNotFoundException("La Ausencia no existe"));
		
		if(!ausencia.getEmpleado().getIdPersona().equals(idEmpleado)) {
			throw new IllegalArgumentException("La ausencia no le pertenece a este empleado");
		}
		
		ausencia.setFecha(dto.getFecha());
		ausencia.setMotivo(dto.getMotivo());
			
		AusenciaEmpleado ausenciaModificada = ausenciaEmpleadoRepository.save(ausencia);
		
		
		return modelMapper.map(ausenciaModificada, AusenciaEmpleadoResponseDTO.class);
	}

	@Override
	public boolean eliminarAusencia(Integer idEmpleado, Integer idAusencia) {
		
		AusenciaEmpleado ausencia = ausenciaEmpleadoRepository.findById(idAusencia)
				.orElseThrow(() -> new EntityNotFoundException("La Ausencia no existe"));
		
		if(!ausencia.getEmpleado().getIdPersona().equals(idEmpleado)) {
			throw new IllegalArgumentException("La ausencia no le pertenece a este empleado");
		}
		
		//TODO: Agregar tryCatch
		ausenciaEmpleadoRepository.delete(ausencia);
		
		return true;
	}

	@Override
	public AusenciaEmpleadoResponseDTO findbyId(Integer id) {
		AusenciaEmpleado ausencia = ausenciaEmpleadoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ausencia con id " + id + " no existe"));
		return modelMapper.map(ausencia,AusenciaEmpleadoResponseDTO.class );
	}
}
