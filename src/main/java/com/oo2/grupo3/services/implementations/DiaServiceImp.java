package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.repositories.IDiaRepository;
import com.oo2.grupo3.services.interfaces.IDiaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiaServiceImp implements IDiaService {

    @Autowired
    private IDiaRepository diaRepository;

    @Override
    public List<Dia> getAll() {
        return diaRepository.findAll();
    }

    @Override
    public Dia findById(Integer id) {
        return diaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Día no encontrado"));
    }
    
    @Override
    public Optional<Dia> findByFecha(LocalDate fecha) {
        return diaRepository.findByFecha(fecha);
    }
    
    public Dia save(Dia dia) {
        return diaRepository.save(dia);
    }

    
}
