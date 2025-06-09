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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/GenerarTurno")
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
    
    
    @GetMapping("/horas/dia/{id}")
    @ResponseBody
    public List<?> obtenerHorasPorDia(@PathVariable Integer id) {
        return horaService.getHorasPorDia(id);
    }

    
    
    @PreAuthorize("hasRole('ADMIN')")
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
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String guardarTurnoDesdeFormulario(@Valid @ModelAttribute("turnoRequest") TurnoRequestDTO turnoRequestDTO,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes,
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

        redirectAttributes.addFlashAttribute("mensaje", "¡Turno generado correctamente!");
        return "redirect:/turnos/confirmacion";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/confirmacion")
    public String mostrarConfirmacion() {
        return "turnos/Confirmacion";
    }

    

}
