package com.oo2.grupo3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Hora;

public interface IHoraRepository extends JpaRepository<Hora, Integer> {
	
}