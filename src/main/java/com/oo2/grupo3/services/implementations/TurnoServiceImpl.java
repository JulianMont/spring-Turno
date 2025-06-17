package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;

import com.oo2.grupo3.repositories.ITurnoRepository;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.repositories.IDiaRepository;
import com.oo2.grupo3.repositories.IHoraRepository;

import com.oo2.grupo3.services.interfaces.ITurnoService;
import com.oo2.grupo3.mappers.TurnoMapper; // 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

