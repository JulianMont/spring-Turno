package com.oo2.grupo3.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Especialidad;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;


@RequestMapping("/empleados")
public class EmpleadoController {

    private final IEmpleadoService empleadoService;
    private final IEspecialidadService especialidadService;

    public EmpleadoController(IEmpleadoService empleadoService, IEspecialidadService especialidadService) {
        this.empleadoService = empleadoService;
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public List<Empleado> findAllEmpleados() {
        return empleadoService.findAllEmpleados();
    }

    @GetMapping("/{id}")
    public Turno findById(@PathVariable Integer id) {
        return empleadoService.findById(id);
    }

    @PostMapping
    public Empleado createEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.crearEmpleado(empleado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable int id) {
        boolean deleted = empleadoService.deleteEmpleado(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/especialidades")
    public List<Especialidad> findAllEspecialidades() {
        return especialidadService.findAllEspecialidades();
    }
}
