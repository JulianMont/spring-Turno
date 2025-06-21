package com.oo2.grupo3.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.models.dtos.requests.AusenciaEmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.AusenciaEmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.AusenciaEmpleado;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.repositories.IAusenciaEmpleadoRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.services.interfaces.IAusenciaEmpleadoService;

import jakarta.transaction.Transactional;


@Service
@Transactional
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
		
		LocalDate fechaMin = LocalDate.now().minusDays(7);
		if(dtoAusencia.getFecha().isBefore(fechaMin)) {
			throw new ErrorValidacionDatosException("Solo se pueden registrar ausencias de los últimos 7 días en adelante");

		}
		
		Empleado empleado = empleadoRepository.findById(idEmpleado)
				.orElseThrow(()-> new EntidadNoEncontradaException("Empleado no encontrado"));
		
	    if (ausenciaEmpleadoRepository.existsByEmpleado_IdPersonaAndFecha(idEmpleado, dtoAusencia.getFecha())) {
	        throw new ErrorValidacionDatosException("Ya existe una ausencia para esa fecha.");
	    }
		
		AusenciaEmpleado ausencia = modelMapper.map(dtoAusencia, AusenciaEmpleado.class);
		ausencia.setEmpleado(empleado);
		
		AusenciaEmpleado ausenciaCreada = ausenciaEmpleadoRepository.save(ausencia);
	
		return modelMapper.map(ausenciaCreada, AusenciaEmpleadoResponseDTO.class);
	}

	@Override
	public AusenciaEmpleadoResponseDTO modificarAusencia(Integer idEmpleado, Integer idAusencia,
			AusenciaEmpleadoRequestDTO dto) {
		
		LocalDate fechaMin = LocalDate.now().minusDays(7);
		if(dto.getFecha().isBefore(fechaMin)) {
			throw new ErrorValidacionDatosException("Solo se pueden modificar ausencias de los últimos 7 días en adelante");

		}
		
		AusenciaEmpleado ausencia = ausenciaEmpleadoRepository.findById(idAusencia)
				.orElseThrow(() -> new EntidadNoEncontradaException("La Ausencia no existe"));
		
		if(!ausencia.getEmpleado().getIdPersona().equals(idEmpleado)) {
			throw new ErrorValidacionDatosException("La ausencia no le pertenece a este empleado");
		}
		
		ausencia.setFecha(dto.getFecha());
		ausencia.setMotivo(dto.getMotivo());
			
		AusenciaEmpleado ausenciaModificada = ausenciaEmpleadoRepository.save(ausencia);
		
		
		return modelMapper.map(ausenciaModificada, AusenciaEmpleadoResponseDTO.class);
	}

	@Override
	public boolean eliminarAusencia(Integer idEmpleado, Integer idAusencia) {
		
		AusenciaEmpleado ausencia = ausenciaEmpleadoRepository.findById(idAusencia)
				.orElseThrow(() -> new EntidadNoEncontradaException("La Ausencia no existe"));
		
		if(!ausencia.getEmpleado().getIdPersona().equals(idEmpleado)) {
			throw new ErrorValidacionDatosException("La ausencia no le pertenece a este empleado");
		}
		
		//TODO: Agregar tryCatch
		ausenciaEmpleadoRepository.delete(ausencia);
		
		return true;
	}

	@Override
	public AusenciaEmpleadoResponseDTO findbyId(Integer id) {
		AusenciaEmpleado ausencia = ausenciaEmpleadoRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("Ausencia con id " + id + " no existe"));
		return modelMapper.map(ausencia,AusenciaEmpleadoResponseDTO.class );
	}
}
