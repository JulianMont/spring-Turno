package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.models.entities.Hora;
import com.oo2.grupo3.repositories.IHoraRepository;
import com.oo2.grupo3.services.interfaces.IHoraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HoraServiceImp implements IHoraService {

    @Autowired
    private IHoraRepository horaRepository;

    @Override
    public List<Hora> getAll() {
        return horaRepository.findAll();
    }

    @Override
    public List<Hora> getHorasPorDia(Integer idDia) {
        return horaRepository.findByDiaId(idDia); 
    }
    
    
    @Override
    public Hora findById(Integer id) {
        return horaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hora no encontrada"));
    }
    
    @Override
    public Hora findByHora(LocalTime hora) {
        List<Hora> horas = horaRepository.findAllByHora(hora);
        if (horas.isEmpty()) {
            throw new RuntimeException("Hora no encontrada: " + hora);
        }
        return horas.get(0);
    }
    
}
