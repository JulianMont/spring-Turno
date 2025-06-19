package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;
import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.services.interfaces.IServicioService;

@Service
public class ServicioServiceImp implements IServicioService {

	
    private final IServicioRepository servicioRepository;

    public ServicioServiceImp(IServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        servicioRepository.findById(id)
            .ifPresent(servicio -> servicioRepository.deleteById(id));
    }

    @Override
    public Optional<Servicio> findById(Integer id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Optional<Servicio> findByNombre(String name) {
        return servicioRepository.findByNombre(name);
    }

    @Override
    @Transactional
    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public Optional<Servicio> findByUbicacion(Ubicacion ubicacion) {
        return servicioRepository.findByUbicacion(ubicacion);
    }

    @Override
    public List<Servicio> getAll() {
        return servicioRepository.findAll();
    }
}
