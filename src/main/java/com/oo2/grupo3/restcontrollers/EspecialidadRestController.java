package com.oo2.grupo3.restcontrollers;

import com.oo2.grupo3.models.dtos.requests.EspecialidadRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "Operaciones CRUD sobre especialidades")
public class EspecialidadRestController {

    private final IEspecialidadService especialidadService;

    @Operation(summary = "Obtener todas las especialidades")
    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> getAll() {
        return ResponseEntity.ok(especialidadService.traerEspecialidades());
    }

    @Operation(summary = "Obtener una especialidad por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.findById(id));
    }

    @Operation(summary = "Crear una nueva especialidad")
    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> create(@Valid @RequestBody EspecialidadRequestDTO dto) {
        return ResponseEntity.ok(especialidadService.crearEspecialidad(dto));
    }

    @Operation(summary = "Actualizar una especialidad existente")
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDTO> update(@PathVariable Long id,
                                                          @Valid @RequestBody EspecialidadRequestDTO dto) {
        return ResponseEntity.ok(especialidadService.editarEspecialidad(id, dto));
    }

    @Operation(summary = "Eliminar una especialidad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        especialidadService.borrarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}
