package com.oo2.grupo3.repositories;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Dia;


@Repository
public interface IDiaRepository extends JpaRepository<Dia, Integer> {
	
	List<Dia> findAllByFecha(LocalDate fecha);
	Optional<Dia> findByFecha(LocalDate fecha);
	
}
