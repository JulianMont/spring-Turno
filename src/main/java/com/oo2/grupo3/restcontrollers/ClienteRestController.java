package com.oo2.grupo3.restcontrollers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo3.models.dtos.record.ClienteRecordDTO;
import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.requests.UserRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.RoleEntity;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.RoleType;
import com.oo2.grupo3.repositories.IRoleRepository;
import com.oo2.grupo3.services.interfaces.IClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    private final IClienteService clienteService;
    private final ModelMapper modelMapper;
    
    private IRoleRepository roleRepository;
    
    @Autowired
    public ClienteRestController(IClienteService clienteService, ModelMapper modelMapper, IRoleRepository roleRepository ) {
        this.clienteService = clienteService;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }
    
    @GetMapping
    public List<ClienteRecordDTO> getAllClientes() {
        return clienteService.getAllClientes().stream()
            .map(cliente -> new ClienteRecordDTO(
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getDni(),
                modelMapper.map(cliente.getUser(), UserRequestDTO.class)
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteRecordDTO getById(@PathVariable Integer id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return new ClienteRecordDTO(
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getDni(),
            modelMapper.map(cliente.getUser(), UserRequestDTO.class)
        );}

   
    @PostMapping
    public ClienteRecordDTO createCliente(@RequestBody ClienteRecordDTO dto) {
        Cliente nuevo = new Cliente();

        nuevo.setNombre(dto.nombre());
        nuevo.setApellido(dto.apellido());
        nuevo.setDni(dto.dni());

        UserEntity user = new UserEntity();
        user.setEmail(dto.user().getEmail());
        user.setPassword(dto.user().getPassword()); // Hashear antes de guardar preferiblemente

        Set<RoleEntity> roles = new HashSet<>();
        for (String rol : dto.user().getRoles()) {
            RoleType tipo = RoleType.valueOf(rol);
            RoleEntity roleEntity = roleRepository.findByType(tipo)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol));
            roles.add(roleEntity);
        }
        user.setRoleEntities(roles);

        nuevo.setUser(user);

        Cliente guardado = clienteService.save(nuevo);

        // Crear UserRequestDTO para respuesta sin devolver password
        UserRequestDTO userDTO = new UserRequestDTO();
        userDTO.setEmail(guardado.getUser().getEmail());
        userDTO.setRoles(guardado.getUser().getRoleEntities().stream()
            .map(role -> role.getType().name())
            .collect(Collectors.toSet()));
        // No seteamos password en respuesta por seguridad

        return new ClienteRecordDTO(
            guardado.getNombre(),
            guardado.getApellido(),
            guardado.getDni(),
            userDTO
        );
    }

}

