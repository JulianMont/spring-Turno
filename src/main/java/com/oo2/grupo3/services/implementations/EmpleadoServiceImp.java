package com.oo2.grupo3.services.implementations;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Especialidad;
import com.oo2.grupo3.repositories.IEspecialidadRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmpleadoServiceImp implements IEmpleadoService {

    private final IEmpleadoRepository empleadoRepository;
    private final IEspecialidadRepository especialidadRepository;
    private final ModelMapper modelMapper;

    public EmpleadoServiceImp(IEmpleadoRepository empleadoRepository,
                              IEspecialidadRepository especialidadRepository,
                              ModelMapper modelMapper) {
        this.empleadoRepository = empleadoRepository;
        this.especialidadRepository = especialidadRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Empleado> getAll() {
        return empleadoRepository.findAll();
    }
    
    @Override
    public Page<EmpleadoResponseDTO> findAll(Pageable pageable) {
        return empleadoRepository.findAll(pageable)
                .map(empleado -> modelMapper.map(empleado, EmpleadoResponseDTO.class));
    }

    @Override
    public Page<EmpleadoResponseDTO> findByIdEspecialidad(Long idEspecialidad, Pageable pageable) {
        return empleadoRepository.findByEspecialidad_IdEspecialidad(idEspecialidad, pageable)
                .map(empleado -> modelMapper.map(empleado, EmpleadoResponseDTO.class));
    }

    @Override
    public Page<EmpleadoResponseDTO> findByNombre(String nombre, Pageable pageable) {
        return empleadoRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(empleado -> modelMapper.map(empleado, EmpleadoResponseDTO.class));
    }

    @Override
    public EmpleadoResponseDTO findById(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no existe"));
        return modelMapper.map(empleado, EmpleadoResponseDTO.class);
    }

    @Override
    public EmpleadoResponseDTO findByLegajo(String legajo) {
        Empleado empleado = empleadoRepository.findByLegajo(legajo)
                .orElseThrow(() -> new EntityNotFoundException("Empleado con legajo " + legajo + " no existe"));
        return modelMapper.map(empleado, EmpleadoResponseDTO.class);
    }

    @Override
    public EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO) {
        Optional<Empleado> empleadoExiste = empleadoRepository.findByLegajo(empleadoRequestDTO.getLegajo());
        if (empleadoExiste.isPresent()) {
            throw new IllegalArgumentException("Ya existe un empleado con el legajo " + empleadoRequestDTO.getLegajo());
        }

        // Buscar la especialidad por id para asignar
        Especialidad especialidad = especialidadRepository.findById(empleadoRequestDTO.getEspecialidadId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Especialidad con id " + empleadoRequestDTO.getEspecialidadId() + " no existe"));

        Empleado empleado = modelMapper.map(empleadoRequestDTO, Empleado.class);
        empleado.setEspecialidad(especialidad);

        Empleado empleadoCreado = empleadoRepository.save(empleado);
        return modelMapper.map(empleadoCreado, EmpleadoResponseDTO.class);
    }

    @Override
    public EmpleadoResponseDTO actualizarEmpleado(Integer idEmpleado, EmpleadoRequestDTO empleadoRequestDTO) {
        Empleado empleadoExiste = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Empleado con id {0} no existe", idEmpleado)));

        // Validar que si se cambia el legajo, no haya otro empleado con ese legajo
        if (!empleadoExiste.getLegajo().equals(empleadoRequestDTO.getLegajo())) {
            Optional<Empleado> empleadoConLegajo = empleadoRepository.findByLegajo(empleadoRequestDTO.getLegajo());
            if (empleadoConLegajo.isPresent() && !empleadoConLegajo.get().getId().equals(idEmpleado)) {
                throw new IllegalArgumentException(
                        "Ya existe otro empleado con el legajo " + empleadoRequestDTO.getLegajo());
            }
        }

        empleadoExiste.setLegajo(empleadoRequestDTO.getLegajo());

        // Buscar y asignar la especialidad
        Especialidad especialidad = especialidadRepository.findById(empleadoRequestDTO.getEspecialidadId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Especialidad con id " + empleadoRequestDTO.getEspecialidadId() + " no existe"));
        empleadoExiste.setEspecialidad(especialidad);

        Empleado empleadoActualizado = empleadoRepository.save(empleadoExiste);
        return modelMapper.map(empleadoActualizado, EmpleadoResponseDTO.class);
    }

    @Override
    public boolean borrarEmpleado(Integer idEmpleado) {
        Empleado empleadoExiste = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Empleado con id {0} no existe", idEmpleado)));
        empleadoRepository.delete(empleadoExiste);
        return true;
    }
}
