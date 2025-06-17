

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

	Page<Empleado> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
	
	Optional<Empleado> findByLegajo(String legajo);
	
	boolean existsByLegajo(String legajo);

}

