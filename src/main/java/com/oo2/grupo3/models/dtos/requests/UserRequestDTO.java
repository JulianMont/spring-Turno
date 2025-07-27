package com.oo2.grupo3.models.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email
    @Schema(description = "Email del usuario", example = "usuario@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    // permitir asignación manual de roles desde panel de admin
    @Schema(description = "Roles del usuario (USER o ADMIN)", example = "[\"ADMIN\"]")
    private Set<String> roles;
}
