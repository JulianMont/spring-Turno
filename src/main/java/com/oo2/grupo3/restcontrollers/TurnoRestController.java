package com.oo2.grupo3.restcontrollers;

import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.record.TurnoRecordDTO;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.services.interfaces.ITurnoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class TurnoRestController {

    @Autowired
    private ITurnoService turnoService;


    @Autowired
    private TurnoMapper turnoMapper;

    @GetMapping("/all")
    public ResponseEntity<List<TurnoResponseDTO>> getAllTurnos() {
        return ResponseEntity.ok(turnoService.obtenerTodosLosTurnos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> getTurnoById(@PathVariable Integer id) {
        Turno turno = turnoService.findById(id);
        return ResponseEntity.ok(turnoMapper.toResponse(turno));
    }

    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crearTurno(@RequestBody @Valid TurnoRequestDTO requestDTO) {
        TurnoResponseDTO creado = turnoService.solicitarTurno(requestDTO);
        return ResponseEntity.ok(creado);
    }

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