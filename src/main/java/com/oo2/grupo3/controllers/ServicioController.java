package com.oo2.grupo3.controllers;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.ServicioRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ServicioResponseDTO;
import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;
import com.oo2.grupo3.services.interfaces.IServicioService;
import com.oo2.grupo3.services.interfaces.IUbicacionService;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final IServicioService servicioService;
    private final IUbicacionService ubicacionService;
    private final ModelMapper modelMapper;

    public ServicioController(IServicioService servicioService, IUbicacionService ubicacionService, ModelMapper modelMapper) {
        this.servicioService = servicioService;
        this.ubicacionService = ubicacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> getById(@PathVariable Integer id) {
        Optional<Servicio> servicioOpt = servicioService.findById(id);
        if (servicioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ServicioResponseDTO dto = modelMapper.map(servicioOpt.get(), ServicioResponseDTO.class);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicioResponseDTO> create(@Valid @RequestBody ServicioRequestDTO dto) {
        Optional<Ubicacion> ubicacion = ubicacionService.findById(dto.getIdUbicacion());
        if (ubicacion.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Servicio servicio = modelMapper.map(dto, Servicio.class);
        servicio.setUbicacion(ubicacion.get());
        Servicio saved = servicioService.save(servicio);
        ServicioResponseDTO responseDto = modelMapper.map(saved, ServicioResponseDTO.class);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevoServicio(Model model) {
        model.addAttribute("servicioRequestDTO", new ServicioRequestDTO());
        model.addAttribute("ubicaciones", ubicacionService.getAll());
        return "servicios/form";
    }

    @PostMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String crearServicio(@Valid @ModelAttribute ServicioRequestDTO dto,
                                BindingResult result,
                                Model model) {
        if (servicioService.findByNombre(dto.getNombre()).isPresent()) {
            model.addAttribute("error", "Ya existe un servicio con ese nombre.");
            model.addAttribute("ubicaciones", ubicacionService.getAll()); // necesario para el combo
            return ViewRouteHelper.SERVICIO_FORM;
        }

        if (result.hasErrors()) {
            model.addAttribute("ubicaciones", ubicacionService.getAll());
            return ViewRouteHelper.SERVICIO_FORM;
        }

        Servicio servicio = modelMapper.map(dto, Servicio.class);
        servicio.setUbicacion(ubicacionService.findById(dto.getIdUbicacion()).orElse(null));
        servicioService.save(servicio);
        return ViewRouteHelper.REDIRECT_SERVICIO_LIST;
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.getAll());
        return "servicios/list";
    }

}

