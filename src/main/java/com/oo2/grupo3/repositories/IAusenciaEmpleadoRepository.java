	package com.oo2.grupo3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.AusenciaEmpleado;

public interface IAusenciaEmpleadoRepository extends JpaRepository<AusenciaEmpleado, Integer> {

	List<AusenciaEmpleado> findByEmpleado_Id(Integer empleadoId);
	
}
