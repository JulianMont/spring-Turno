package com.oo2.grupo3.controllers;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.services.interfaces.ITurnoService;
import com.oo2.grupo3.mappers.TurnoMapper;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @Autowired
    private TurnoMapper turnoMapper;

    @GetMapping
    public List<TurnoResponseDTO> findAll() {
        return turnoService.findAll().stream()
                .map(TurnoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TurnoResponseDTO findById(@PathVariable Integer id) {
        Turno turno = turnoService.findById(id);
        return TurnoMapper.toResponse(turno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        turnoService.deleteById(id);
        return ResponseEntity.ok("Turno eliminado correctamente.");
    }

    @PostMapping("/generar")
    public Turno generarTurno(
            @RequestParam Integer idCliente,
            @RequestParam Integer idEmpleado,
            @RequestParam Integer idServicio,
            @RequestParam Integer idDia,
            @RequestParam Integer idHora
    ) {
        return turnoService.generarTurno(idCliente, idEmpleado, idServicio, idDia, idHora);
    }

    // ✅ Método definitivo para guardar un turno usando DTOs
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> save(@RequestBody @Valid TurnoRequestDTO requestDTO) {
        Turno turno = turnoMapper.toEntity(requestDTO);
        Turno saved = turnoService.save(turno);
        return ResponseEntity.ok(turnoMapper.toResponse(saved));
    }
}
