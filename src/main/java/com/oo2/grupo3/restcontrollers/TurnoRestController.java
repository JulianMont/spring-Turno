package com.oo2.grupo3.restcontrollers;

import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.record.TurnoRecordDTO;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.services.interfaces.ITurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@Tag(name = "Turnos", description = "Operaciones CRUD sobre turnos")
public class TurnoRestController {

    @Autowired
    private ITurnoService turnoService;


    @Autowired
    private TurnoMapper turnoMapper;

    @Operation(summary = "Trae todos los turnos")
    @GetMapping("/all")
    public ResponseEntity<List<TurnoResponseDTO>> getAllTurnos() {
        return ResponseEntity.ok(turnoService.obtenerTodosLosTurnos());
    }

    @Operation(summary = "Trae el turno seleccionado por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> getTurnoById(@PathVariable Integer id) {
        Turno turno = turnoService.findById(id);
        return ResponseEntity.ok(turnoMapper.toResponse(turno));
    }

    @Operation(summary = "Crea un nuevo turno.")
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crearTurno(@RequestBody @Valid TurnoRequestDTO requestDTO) {
        TurnoResponseDTO creado = turnoService.solicitarTurno(requestDTO);
        return ResponseEntity.ok(creado);
    }

    @Operation(summary = "Modifica la fecha y hora del turno. Para que no haya error, la fecha y hora deben ingresarse de la siguiente manera: ")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarTurno(
            @PathVariable Integer id,
            @RequestBody @Valid TurnoRecordDTO dto) {

        try {
            LocalDate fecha = LocalDate.parse(dto.dia());  
            LocalTime hora = LocalTime.parse(dto.hora());
            turnoService.actualizarFechaYHora(id, fecha, hora);
            return ResponseEntity.ok("Turno actualizado correctamente.");
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha u hora inválido. Usa 'YYYY-MM-DD' y 'HH:mm'");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarTurno(@PathVariable Integer id) {
        turnoService.cancelarTurno(id);
        return ResponseEntity.ok("Turno cancelado correctamente.");
    }
    
}