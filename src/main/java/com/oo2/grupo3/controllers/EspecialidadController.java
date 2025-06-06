package com.oo2.grupo3.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo3.models.entities.Especialidad;
import com.oo2.grupo3.repositories.IEspecialidadRepository;

@RequestMapping("/api")
public class EspecialidadController {

    private final IEspecialidadRepository especialidadRepository;


    public EspecialidadController(IEspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @GetMapping("/especialidades")
    public List<Especialidad> listarEspecialidades() {
        return especialidadRepository.findAll();
    }
}