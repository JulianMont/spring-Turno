package com.oo2.grupo3.controllers;

import com.oo2.grupo3.mappers.TurnoMapper;
import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.models.enums.EstadoTurno;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.ITurnoRepository;
import com.oo2.grupo3.services.interfaces.IClienteService;
import com.oo2.grupo3.services.interfaces.IDiaService;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IHoraService;
import com.oo2.grupo3.services.interfaces.IServicioService;
import com.oo2.grupo3.services.interfaces.ITurnoService;
import com.oo2.grupo3.helpers.ViewRouteHelper;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarTurno(@PathVariable Integer id, Model model) {
        Turno turno = turnoService.findById(id);
        TurnoRequestDTO turnoRequest = turnoMapper.toRequest(turno);

        model.addAttribute("turnoRequest", turnoRequest);
        model.addAttribute("idTurno", id);
        model.addAttribute("nombreCliente", turno.getCliente().getNombreCompleto());
        model.addAttribute("nombreEmpleado", turno.getEmpleado().getNombreCompleto());
        model.addAttribute("nombreServicio", turno.getServicio().getNombre());
        model.addAttribute("horas", horaService.getAll());

        return "turnos/EditarTurno";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/turnos/cancelar/{id}")
    public String cancelarTurno(@PathVariable Integer id) {
        turnoService.cancelarTurno(id);
        return ViewRouteHelper.TURNO_LIST_REDIRECT;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> save(@RequestBody @Valid TurnoRequestDTO requestDTO) {
        Turno turno = turnoMapper.toEntity(requestDTO);
        Turno saved = turnoService.save(turno);
        return ResponseEntity.ok(turnoMapper.toResponse(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
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
        model.addAttribute("turnoRequest", turnoRequest);
        model.addAttribute("clientes", clienteService.getAllClientes());
        model.addAttribute("empleados", empleadoService.getAllEmpleados());
        model.addAttribute("servicios", servicioService.getAll());
        model.addAttribute("dias", diaService.getAll());
        model.addAttribute("horas", horaService.getAll());

        return ViewRouteHelper.TURNO_GENERAR;
    }

    @PreAuthorize("hasRole('ADMIN')") // pasarlo a CLIENTE
    @PostMapping("/GenerarTurno")
    public String guardarTurnoDesdeFormulario(@Valid @ModelAttribute("turnoRequest") TurnoRequestDTO turnoRequestDTO,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes,
                                              Model model) {

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
            return ViewRouteHelper.TURNO_GENERAR;
        }

        try {
            turnoService.save(turnoRequestDTO);
            redirectAttributes.addFlashAttribute("mensaje", "¡Turno generado correctamente!");
            return ViewRouteHelper.TURNO_LIST_REDIRECT;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al generar el turno: " + e.getMessage());
            model.addAttribute("clientes", clienteService.getAllClientes());
            model.addAttribute("empleados", empleadoService.getAllEmpleados());
            model.addAttribute("servicios", servicioService.getAll());
            model.addAttribute("dias", diaService.getAll());
            model.addAttribute("horas", horaService.getAll());
            return ViewRouteHelper.TURNO_GENERAR;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar/{id}")
    public String editarTurno(@PathVariable Integer id,
                              @Valid @ModelAttribute("turnoRequest") TurnoRequestDTO turnoRequestDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (turnoRequestDTO.getFecha() == null) {
            bindingResult.rejectValue("fecha", "error.turnoRequestDTO", "La fecha es obligatoria");
        }

        if (turnoRequestDTO.getHora() == null) {
            bindingResult.rejectValue("hora", "error.turnoRequestDTO", "La hora es obligatoria");
        }

        if (!bindingResult.hasErrors()) {
            DayOfWeek diaSemana = turnoRequestDTO.getFecha().getDayOfWeek();
            if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
                bindingResult.rejectValue("fecha", "error.turnoRequestDTO", "No se permiten turnos los fines de semana.");
            }

            int minutos = turnoRequestDTO.getHora().getMinute();
            if (minutos != 0 && minutos != 30) {
                bindingResult.rejectValue("hora", "error.turnoRequestDTO", "Solo se permiten intervalos de 30 minutos.");
            }

            int hora = turnoRequestDTO.getHora().getHour();
            if (hora < 8 || hora > 19 || (hora == 19 && minutos == 30)) {
                bindingResult.rejectValue("hora", "error.turnoRequestDTO", "Horario fuera del rango permitido (08:00 a 20:00).");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("idTurno", id);
            model.addAttribute("horas", horaService.getAll());

            Turno turno = turnoService.findById(id);
            model.addAttribute("nombreCliente", turno.getCliente().getNombreCompleto());
            model.addAttribute("nombreEmpleado", turno.getEmpleado().getNombreCompleto());
            model.addAttribute("nombreServicio", turno.getServicio().getNombre());

            return "turnos/EditarTurno";
        }

        try {
            turnoService.actualizarFechaYHora(id, turnoRequestDTO.getFecha(), turnoRequestDTO.getHora());
            redirectAttributes.addFlashAttribute("mensaje", "Turno actualizado correctamente.");
            return ViewRouteHelper.TURNO_LIST_REDIRECT;
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el turno: " + e.getMessage());
            model.addAttribute("idTurno", id);
            model.addAttribute("horas", horaService.getAll());

            Turno turno = turnoService.findById(id);
            model.addAttribute("nombreCliente", turno.getCliente().getNombreCompleto());
            model.addAttribute("nombreEmpleado", turno.getEmpleado().getNombreCompleto());
            model.addAttribute("nombreServicio", turno.getServicio().getNombre());

            return "turnos/EditarTurno";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public String listarTurnos(
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) Integer empleadoId,
            @RequestParam(required = false) Integer servicioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        List<TurnoResponseDTO> turnos = turnoService.obtenerTodosLosTurnos();

        if (clienteId != null) {
            turnos = turnos.stream()
                    .filter(t -> clienteId.equals(t.getIdCliente()))
                    .collect(Collectors.toList());
        }

        if (empleadoId != null) {
            turnos = turnos.stream()
                    .filter(t -> empleadoId.equals(t.getIdEmpleado()))
                    .collect(Collectors.toList());
        }

        if (servicioId != null) {
            turnos = turnos.stream()
                    .filter(t -> servicioId.equals(t.getIdServicio()))
                    .collect(Collectors.toList());
        }

        if (fecha != null) {
            turnos = turnos.stream()
                    .filter(t -> fecha.equals(t.getDia()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("turnos", turnos);
        model.addAttribute("clientes", clienteService.getAllClientes());
        model.addAttribute("empleados", empleadoService.getAllEmpleados());
        model.addAttribute("servicios", servicioService.getAll());

        model.addAttribute("clienteId", clienteId);
        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("servicioId", servicioId);
        model.addAttribute("fecha", fecha);

        return ViewRouteHelper.TURNO_LIST;
    }

}
