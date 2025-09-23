package com.oo2.grupo3.restcontrollers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestion de Clientes")
public class ClienteRestController {

    private final IClienteService clienteService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    
    private IRoleRepository roleRepository;
    
    @Autowired
    public ClienteRestController(IClienteService clienteService, ModelMapper modelMapper, PasswordEncoder passwordEncoder,IRoleRepository roleRepository ) {
        this.clienteService = clienteService;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Operation(summary = "Obtener todos los clientes")
    @GetMapping 
    public List<ClienteRecordDTO> getAllClientes() {
        return clienteService.getAllClientes().stream()
            .map(cliente -> new ClienteRecordDTO(
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getDni(),
                cliente.getUser() != null ? modelMapper.map(cliente.getUser(), UserRequestDTO.class) : null
            ))
            .collect(Collectors.toList());
    }
    @Operation(summary = "Obtener a un cliente en especifico")
    @GetMapping("/{id}")
    public ClienteRecordDTO getById(@PathVariable Integer id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return new ClienteRecordDTO(
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getDni(),
            modelMapper.map(cliente.getUser(), UserRequestDTO.class)
        );}
    
    @Operation(summary = "Crear Cliente")
    @PostMapping
    public ClienteRecordDTO createCliente(@RequestBody ClienteRecordDTO dto) {

        
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setDni(dto.dni());

     
        UserEntity user = new UserEntity();
        user.setEmail(dto.user().getEmail());
        user.setPassword(passwordEncoder.encode(dto.user().getPassword()));
        user.setPersona(cliente);

        
        RoleEntity rolUser = roleRepository.findByType(RoleType.USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.setRoleEntities(Set.of(rolUser));

        
        cliente.setUser(user);

        
        Cliente guardado = clienteService.save(cliente);

        
        UserRequestDTO userDTO = new UserRequestDTO();
        userDTO.setEmail(guardado.getUser().getEmail());
        userDTO.setRoles(guardado.getUser().getRoleEntities().stream()
                .map(role -> role.getType().name())
                .collect(Collectors.toSet()));

        return new ClienteRecordDTO(
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getDni(),
                userDTO
        );
    }



    
    @Operation(summary = "Eliminar un cliente por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable Integer id) {
        boolean eliminado = clienteService.remove(id);
        if (eliminado) {
            return ResponseEntity.ok("Cliente eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }
    }


}

