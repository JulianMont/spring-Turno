package com.oo2.grupo3.repositories;

import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Hora;
import com.oo2.grupo3.models.entities.Turno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ITurnoRepository extends JpaRepository<Turno, Integer> {

	boolean existsByEmpleadoAndDiaAndHora(Empleado empleado, Dia dia, Hora hora);

	boolean existsByClienteAndDiaAndHora(Cliente cliente, Dia dia, Hora hora);

	boolean existsByEmpleadoAndDiaAndHoraAndIdTurnoNot(Empleado empleado, Dia dia, Hora hora, Integer idTurnoActual);

	boolean existsByClienteAndDiaAndHoraAndIdTurnoNot(Cliente cliente, Dia dia, Hora hora, Integer idTurnoActual);

	
	
}