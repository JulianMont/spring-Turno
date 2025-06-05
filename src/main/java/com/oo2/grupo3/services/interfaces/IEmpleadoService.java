package com.oo2.grupo3.services.interfaces;

import java.util.List;

import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Turno;

public interface IEmpleadoService {

	List<Empleado> listarEmpleados();

	Empleado crearEmpleado(Empleado empleado);

	Object actualizarEmpleado(int id, Empleado empleadoDetalles);

	boolean eliminarEmpleado(int id);

	List<Empleado> findAllEmpleados();

	Object findEmpleadoById(int id);

	boolean deleteEmpleado(int id);

	Object updateEmpleado(int id, Empleado empleadoDetalles);

	Turno findById(Integer id);

}
