package com.oo2.grupo3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Especialidad;


@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Integer> {
	
}