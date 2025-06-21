package com.oo2.grupo3.models.dtos.responses;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	 
	    private String email;

	    private String password;
	    // permitir asignación manual de roles desde panel de admin
	    private Set<String> roles;
}
