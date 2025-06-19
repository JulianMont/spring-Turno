package com.oo2.grupo3.controllers;

import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;

import com.oo2.grupo3.models.entities.Cliente;

import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.ITurnoRepository;
import com.oo2.grupo3.services.interfaces.IClienteService;
import com.oo2.grupo3.services.interfaces.IDiaService;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IHoraService;
import com.oo2.grupo3.services.interfaces.IServicioService;
import com.oo2.grupo3.services.interfaces.ITurnoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    // Servicios
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

    // Mapper
    @Autowired
    private TurnoMapper turnoMapper;

    // --- API REST ---
     
    
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

    // --- Formulario WEB ---

    @PreAuthorize("hasRole('ADMIN')") // pasarlo a CLIENTE
    @GetMapping("/GenerarTurno")
    public String mostrarFormularioTurno(Model model, Principal principal) {
        TurnoRequestDTO turnoRequest = new TurnoRequestDTO();
/*
        Cliente clienteLogueado = clienteService.findByUsername(principal.getName());
        turnoRequest.setIdCliente(clienteLogueado.getIdPersona()); // setear el idCliente
*/
        model.addAttribute("turnoRequest", turnoRequest);

        model.addAttribute("clientes", clienteService.getAllClientes());
        model.addAttribute("empleados", empleadoService.getAllEmpleados());
        model.addAttribute("servicios", servicioService.getAll());
        model.addAttribute("dias", diaService.getAll());
        model.addAttribute("horas", horaService.getAll());

        return "turnos/GenerarTurno";
    }
    
    /*

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
*/
  
    @PreAuthorize("hasRole('ADMIN')") // pasarlo a CLIENTE

    @PostMapping("/GenerarTurno")
    public String guardarTurnoDesdeFormulario(@Valid @ModelAttribute("turnoRequest") TurnoRequestDTO turnoRequestDTO,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes,
                                              Model model) {


        // Validaciones iniciales
        if (turnoRequestDTO.getFecha() == null) {
            bindingResult.rejectValue("fecha", "error.turnoRequestDTO", "La fecha es obligatoria");
        }
        if (turnoRequestDTO.getHora() == null) {
            bindingResult.rejectValue("hora", "error.turnoRequestDTO", "La hora es obligatoria");
        }

        if (!bindingResult.hasErrors()) {
            DayOfWeek diaSemana = turnoRequestDTO.getFecha().getDayOfWeek();
            if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
                bindingResult.rejectValue("fecha", "error.turnoRequestDTO", "No se pueden generar turnos los sábados ni domingos.");
            }

            int minutos = turnoRequestDTO.getHora().getMinute();
            if (minutos != 0 && minutos != 30) {
                bindingResult.rejectValue("hora", "error.turnoRequestDTO", "Los turnos solo pueden ser en intervalos de 30 minutos.");
            }

            int hora = turnoRequestDTO.getHora().getHour();
            if (hora < 8 || hora > 19 || (hora == 19 && minutos == 30)) {
                bindingResult.rejectValue("hora", "error.turnoRequestDTO", "Los turnos solo pueden generarse entre las 08:00 y las 20:00.");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("clientes", clienteService.getAllClientes());
            model.addAttribute("empleados", empleadoService.getAllEmpleados());
            model.addAttribute("servicios", servicioService.getAll());
            model.addAttribute("dias", diaService.getAll());
            model.addAttribute("horas", horaService.getAll());
            return "turnos/GenerarTurno";
        }

        try {
            turnoService.save(turnoRequestDTO);  // Enviar DTO directamente
            redirectAttributes.addFlashAttribute("mensaje", "¡Turno generado correctamente!");
            return "redirect:/turnos/list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al generar el turno: " + e.getMessage());
            model.addAttribute("clientes", clienteService.getAllClientes());
            model.addAttribute("empleados", empleadoService.getAllEmpleados());
            model.addAttribute("servicios", servicioService.getAll());
            model.addAttribute("dias", diaService.getAll());
            model.addAttribute("horas", horaService.getAll());
            return "turnos/GenerarTurno";
        }
    }

    
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public String listarTurnos(Model model) {
        List<TurnoResponseDTO> turnos = turnoService.getAll().stream()
            .map(turnoMapper::toResponse)
            .collect(Collectors.toList());
        model.addAttribute("turnos", turnos);


        model.addAttribute("clientes", clienteService.getAllClientes());
        model.addAttribute("empleados", empleadoService.getAllEmpleados());
        model.addAttribute("servicios", servicioService.getAll());

        return "turnos/list"; 

    }
}
