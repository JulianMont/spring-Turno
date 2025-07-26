package com.oo2.grupo3.restcontrollers;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo3.models.dtos.record.LoginRequestRecordDTO;
import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.services.interfaces.IClienteService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para login y registro.")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final IClienteService clienteService;

    public AuthRestController(AuthenticationManager authenticationManager, IClienteService clienteService) {
        this.authenticationManager = authenticationManager;
        this.clienteService = clienteService;
    }

    @Operation(summary = "Login", description = "Iniciar sesión con email y contraseña.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestRecordDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            return ResponseEntity.ok(Map.of("message", "cliente logueado"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarClienteRest(@RequestBody @Valid ClienteRequestDTO clienteDTO) {
        try {
            clienteService.save(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "cliente registrado correctamente"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un cliente con ese email o DNI."));
        }
    }
    
}
