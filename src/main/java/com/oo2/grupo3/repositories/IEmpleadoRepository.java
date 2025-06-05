package com.oo2.grupo3.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Empleado;

public interface IEmpleadoRepository extends JpaRepository<Empleado, Serializable> {
	
	public abstract Optional <Empleado> findById (int id);
	
	public abstract Optional <Empleado> findByName (String name);

}
