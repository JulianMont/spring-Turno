package com.oo2.grupo3.controllers;

import com.oo2.grupo3.services.interfaces.ITurnoService;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IClienteService;
import com.oo2.grupo3.services.interfaces.IServicioService;
import com.oo2.grupo3.services.interfaces.IDiaService;
import com.oo2.grupo3.services.interfaces.IHoraService;

import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Turno;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @Autowired
    private IEmpleadoService empleadoService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private IDiaService diaService;

    @Autowired
    private IHoraService horaService;

    @Autowired
    private TurnoMapper turnoMapper;

    @GetMapping
    public List<TurnoResponseDTO> findAll() {
        return turnoService.findAll().stream()
                .map(turnoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TurnoResponseDTO findById(@PathVariable Integer id) {
        Turno turno = turnoService.findById(id);
        return turnoMapper.toResponse(turno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        turnoService.deleteById(id);
        return ResponseEntity.ok("Turno eliminado correctamente.");
    }
/*
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


    @PostMapping
    public ResponseEntity<TurnoResponseDTO> save(@RequestBody @Valid TurnoRequestDTO requestDTO) {
        Turno turno = turnoMapper.toEntity(requestDTO);
        Turno saved = turnoService.save(turno);
        return ResponseEntity.ok(turnoMapper.toResponse(saved));
    }

    
    
    // --- NUEVOS MÉTODOS PARA FORMULARIO ---

    @GetMapping("/crear")
    public String mostrarFormularioTurno(Model model) {
        model.addAttribute("turnoRequest", new TurnoRequestDTO());
        model.addAttribute("empleados", empleadoService.getAll());
        model.addAttribute("clientes", clienteService.getAll());



    @GetMapping("/GenerarTurno")
    public String mostrarFormularioTurno(Model model) {
        model.addAttribute("turnoRequest", new TurnoRequestDTO());
        model.addAttribute("clientes", clienteService.getAll());
        model.addAttribute("empleados", empleadoService.getAll());

        model.addAttribute("servicios", servicioService.getAll());
        model.addAttribute("dias", diaService.getAll());
        model.addAttribute("horas", horaService.getAll()); 


        return "turnos/GenerarTurno";

    }

    @PostMapping("/guardar")
    public String guardarTurnoDesdeFormulario(@Valid @ModelAttribute("turnoRequest") TurnoRequestDTO turnoRequestDTO,
                                              BindingResult bindingResult,
                                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("empleados", empleadoService.getAll());
            model.addAttribute("clientes", clienteService.getAll());
            model.addAttribute("servicios", servicioService.getAll());
            model.addAttribute("dias", diaService.getAll());
            model.addAttribute("horas", horaService.getAll());

            return "turnos/GenerarTurno";


        }

        Turno turno = turnoMapper.toEntity(turnoRequestDTO);
        turnoService.save(turno);

        return "redirect:/turnos";
    }

    @GetMapping("/horas/dia/{id}")
    @ResponseBody
    public List<?> obtenerHorasPorDia(@PathVariable Integer id) {
        return horaService.getHorasPorDia(id);
    }

        model.addAttribute("mensaje", "¡Turno generado correctamente!");
        return "turnos/Confirmacion";
        
    }

}
