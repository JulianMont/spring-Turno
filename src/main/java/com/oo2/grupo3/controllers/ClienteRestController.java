package com.oo2.grupo3.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.services.interfaces.IClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    private final IClienteService clienteService;
    private final ModelMapper modelMapper;

    public ClienteRestController(IClienteService clienteService, ModelMapper modelMapper) {
        this.clienteService = clienteService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Obtener todos los clientes ordenados por nombre")
    @ApiResponse(responseCode = "200", description = "Clientes listados correctamente")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAll() {
        List<ClienteResponseDTO> clientes = clienteService.ordenadosPorNombre();
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Obtener cliente por ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable int id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Crear nuevo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente creado correctamente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteRequestDTO clienteDTO) {
        ClienteResponseDTO creado = clienteService.save(clienteDTO);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar cliente por ID")
    @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        clienteService.remove(id);
        return ResponseEntity.noContent().build();
    }
}

