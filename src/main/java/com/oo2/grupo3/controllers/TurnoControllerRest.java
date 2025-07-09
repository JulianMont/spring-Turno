package com.oo2.grupo3.controllers;

import com.oo2.grupo3.mappers.TurnoMapper;
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

@RestController
@RequestMapping("/api/turnos")
//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class TurnoControllerRest {

    @Autowired
    private ITurnoService turnoService;


    @Autowired
    private TurnoMapper turnoMapper;

    @GetMapping
    public ResponseEntity<Page<TurnoResponseDTO>> getAllTurnos(Pageable pageable) {
        return ResponseEntity.ok(turnoService.findAll(pageable));
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

    @PatchMapping("/{id}")
    public ResponseEntity<String> editarTurno(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime nuevaHora) {
        turnoService.actualizarFechaYHora(id, nuevaFecha, nuevaHora);
        return ResponseEntity.ok("Turno actualizado correctamente.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarTurno(@PathVariable Integer id) {
        turnoService.cancelarTurno(id);
        return ResponseEntity.ok("Turno cancelado correctamente.");
    }
    
}