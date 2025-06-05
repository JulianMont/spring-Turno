package com.oo2.grupo3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Dia;

public interface IDiaRepository extends JpaRepository<Dia, Integer> {
	
}