

package com.oo2.grupo3.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Empleado;



import java.util.Optional;

@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {
	
	//busqueda con filtros incluido
    @Query("SELECT e FROM Empleado e "
            + "WHERE (:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) "
            + "AND (:legajo IS NULL OR e.legajo = :legajo) "
            + "AND (:especialidadId IS NULL OR e.especialidad.id = :especialidadId)")
		Page<Empleado> buscarEmpleadosFiltrados(@Param("nombre") String nombre,@Param("legajo") String legajo, @Param("especialidadId") Long especialidadId, Pageable pageable
		);
	
	
	//filtros viejos
	Page<Empleado> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
	
	Optional<Empleado> findByLegajo(String legajo);
	Optional<Empleado> findByDni(int dni);
	
	Page<Empleado> findByEspecialidad_IdEspecialidad(Long idEspecialidad, Pageable pageable);
	
	boolean existsByLegajo(String legajo);

}

