package com.oo2.grupo3.restcontrollers;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.models.dtos.record.EmpleadoRecordDTO;
import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/empleados")

@Tag(name = "Empleados", description = "Gestion de Empleados")
public class EmpleadoRestController {

    private final IEmpleadoService empleadoService;
    public EmpleadoRestController(IEmpleadoService empleadoService,ModelMapper modelMapper) {
    	this.empleadoService = empleadoService;
    }

    @Operation(summary = "Listar empleados con filtros")
    @GetMapping
    public ResponseEntity<?> listarEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String legajo,
            @RequestParam(required = false) Long especialidadId,
            @ParameterObject Pageable pageable) {
        Page<EmpleadoResponseDTO> empleados = empleadoService.buscarEmpleadosFiltrados(nombre, legajo, especialidadId, pageable);
        
        Page<EmpleadoRecordDTO> empleadosRecord = empleados.map(empleado -> new EmpleadoRecordDTO(
                empleado,               
                empleado.getLegajo(),
                empleado.getEspecialidad(),
                empleado.getHorariosLaborales(),
                empleado.getDiasAusentes()
            ));
        
  
        return ResponseEntity.ok(empleadosRecord);
    }
    
   
    @Operation(summary = "Obtener detalle de empleado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> verDetalleEmpleado(@PathVariable Integer id) {
        try {
            EmpleadoResponseDTO empleado = empleadoService.findById(id);
            EmpleadoRecordDTO empleadoRecord = new EmpleadoRecordDTO(
                empleado,
                empleado.getLegajo(),
                empleado.getEspecialidad(),
                empleado.getHorariosLaborales(),
                empleado.getDiasAusentes()
            );
            return ResponseEntity.ok(empleadoRecord);
        } catch (EntidadNoEncontradaException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception ex) {
            Map<String, String> error = Map.of("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Crear nuevo empleado")
    @PostMapping
    public ResponseEntity<?> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO dto) {
    	
    	try {
            EmpleadoResponseDTO creado = empleadoService.createEmpleado(dto);
            
            

            EmpleadoRecordDTO empleadoRecord = new EmpleadoRecordDTO(
            		creado,
                    creado.getLegajo(),
                    creado.getEspecialidad(),
                    creado.getHorariosLaborales(),
                    creado.getDiasAusentes()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(empleadoRecord);

        } catch (EntidadNoEncontradaException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));

        } catch (ErrorValidacionDatosException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }
}
