

package com.oo2.grupo3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Especialidad;

public interface IEspecialidadRepository extends JpaRepository<Especialidad, Long> {
	
	boolean existsByNombreIgnoreCase(String nombre);
    
}

