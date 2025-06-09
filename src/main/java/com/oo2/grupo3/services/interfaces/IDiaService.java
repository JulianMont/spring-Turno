package com.oo2.grupo3.services.interfaces;

import com.oo2.grupo3.models.entities.Dia;
import java.util.List;

public interface IDiaService {
    List<Dia> getAll();
    Dia findById(Integer id);
}
