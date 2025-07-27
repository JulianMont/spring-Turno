package com.oo2.grupo3.restcontrollers;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.models.dtos.record.EspecialidadRecordDTO;
import com.oo2.grupo3.models.dtos.requests.EspecialidadRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/especialidades")

@Tag(name = "Especialidades", description = "Gestion de Especialidades")
public class EspecialidadRestController {

    private final IEspecialidadService especialidadService;
    
    public EspecialidadRestController(IEspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @Operation(summary = "Obtener todas las especialidades")
    @GetMapping
    public ResponseEntity<List<EspecialidadRecordDTO>> getAll() {
    	List<EspecialidadRecordDTO> listaRecord = especialidadService.traerEspecialidades()
    			.stream()
    	        .map(e -> new EspecialidadRecordDTO(e.getIdEspecialidad(), e.getNombre()))
    	        .toList();
        return ResponseEntity.ok(listaRecord);
    }

    @Operation(summary = "Obtener una especialidad por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            EspecialidadResponseDTO response = especialidadService.findById(id);
            EspecialidadRecordDTO espRecord = new EspecialidadRecordDTO(response.getIdEspecialidad(), response.getNombre());
            return ResponseEntity.ok(espRecord);
        } catch (EntidadNoEncontradaException ex) {
        	
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @Operation(summary = "Crear una nueva especialidad")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EspecialidadRequestDTO dto) {
        try {
            EspecialidadResponseDTO responseDTO = especialidadService.crearEspecialidad(dto);
            EspecialidadRecordDTO espRecord = new EspecialidadRecordDTO(responseDTO.getIdEspecialidad(), responseDTO.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(espRecord);
        } catch (ErrorValidacionDatosException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

    
}
