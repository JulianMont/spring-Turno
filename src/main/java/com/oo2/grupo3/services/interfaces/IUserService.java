package com.oo2.grupo3.services.interfaces;

import java.util.Set;

import com.oo2.grupo3.models.dtos.requests.UserRequestDTO;
import com.oo2.grupo3.models.entities.Persona;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.RoleType;

public interface IUserService {

    UserEntity createUser(UserRequestDTO userRequestDTO, RoleType user, Persona persona);

	UserEntity updateUser(Integer userId, UserRequestDTO userRequestDTO);

	void assignRolesToUser(Integer userId, Set<String> roles) throws Exception;




}
