
package com.oo2.grupo3.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Empleado;



import java.util.Optional;

@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {
	
	Page<Empleado> findByEspecialidad_IdEspecialidad(Long idEspecialidad, Pageable pageable);

	Page<Empleado> findByNameContainingIgnoreCase(String nombre, Pageable pageable);
	
	//para cuando el admin busque algun trabajador en especifico
	//lo usa la parte de gestion de empleado
	Optional<Empleado> findByLegajo(String legajo);

}