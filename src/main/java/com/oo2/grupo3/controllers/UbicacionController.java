package com.oo2.grupo3.controllers;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.UbicacionRequestDTO;
import com.oo2.grupo3.models.entities.Ubicacion;
import com.oo2.grupo3.services.interfaces.IUbicacionService;

import jakarta.validation.Valid;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ubicaciones")
public class UbicacionController {

    private final IUbicacionService ubicacionService;
    private final ModelMapper modelMapper;

    public UbicacionController(IUbicacionService ubicacionService, ModelMapper modelMapper) {
        this.ubicacionService = ubicacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioUbicacion(Model model) {
        model.addAttribute("ubicacionRequestDTO", new UbicacionRequestDTO());
        return ViewRouteHelper.UBICACION_FORM;
    }

    @PostMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String crearUbicacion(
        @Valid @ModelAttribute UbicacionRequestDTO dto,
        BindingResult result,
        Model model) {

        if (result.hasErrors()) {
            return ViewRouteHelper.UBICACION_FORM;
        }

        if (ubicacionService.existeUbicacion(dto.getDireccion(), dto.getCiudad())) {
            model.addAttribute("errorMensaje", "Ya existe una ubicación con esa dirección en esa ciudad.");
            return ViewRouteHelper.UBICACION_FORM;
        }

        Ubicacion ubicacion = modelMapper.map(dto, Ubicacion.class);

        try {
            ubicacionService.save(ubicacion);
        } catch (Exception e) {
            model.addAttribute("errorMensaje", e.getMessage());
            return ViewRouteHelper.UBICACION_FORM;
        }

        return ViewRouteHelper.REDIRECT_UBICACION_LIST;
    }

    
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listarUbicaciones(Model model) {
        List<Ubicacion> ubicaciones = ubicacionService.getAll();
        model.addAttribute("ubicaciones", ubicaciones);
        return ViewRouteHelper.UBICACION_LIST;
    }
    
}
