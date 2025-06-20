package com.oo2.grupo3.services.implementations;

import com.oo2.grupo3.models.dtos.requests.UserRequestDTO;
import com.oo2.grupo3.models.entities.RoleEntity;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.RoleType;
import com.oo2.grupo3.repositories.IRoleRepository;
import com.oo2.grupo3.repositories.IUserRepository;
import com.oo2.grupo3.services.interfaces.IUserService;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImp implements UserDetailsService, IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public UserServiceImp( IUserRepository userRepository,IRoleRepository roleRepository,@Lazy PasswordEncoder passwordEncoder,ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(MessageFormat.format("User with email {0} not found", username))
        );
    }

	@Override
	public UserEntity createUser(UserRequestDTO userRequestDTO, RoleType role){
		
		if(userRepository.existsByEmail(userRequestDTO.getEmail())) {
			throw new IllegalArgumentException("El email ya está en uso: " + userRequestDTO.getEmail());
		}
		
		UserEntity user = modelMapper.map(userRequestDTO,UserEntity.class);
		
		//encriptar password
		user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
		
		RoleEntity rolDefault = roleRepository.findByType(role)
	            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + role));


		user.setRoleEntities(Set.of(rolDefault));
		
		return userRepository.save(user);
	}
	
	@Override
	public UserEntity updateUser(Integer userId, UserRequestDTO userRequestDTO) throws Exception {
		
		 UserEntity userExistente = userRepository.findById(userId)
	                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + userId));

	        if (!userExistente.getEmail().equals(userRequestDTO.getEmail())
	                && userRepository.existsByEmail(userRequestDTO.getEmail())) {
	            throw new IllegalArgumentException("El email ya está en uso: " + userRequestDTO.getEmail());
	        }

	        userExistente.setEmail(userRequestDTO.getEmail());

	        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
	            userExistente.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
	        }

	        return userRepository.save(userExistente);
	}

    @Override
    public void assignRolesToUser(Integer userId, Set<String> roles) throws Exception {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id " + userId));

        Set<RoleEntity> newRoles = roles.stream()
                .map(r -> roleRepository.findByType(RoleType.valueOf(r.toUpperCase()))
                        .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + r)))
                .collect(Collectors.toSet());

        user.setRoleEntities(newRoles);
        userRepository.save(user);
    }
    
    
//    @Override
//    public void deleteUser(Integer userId) throws Exception {
//        UserEntity user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + userId + " no existe"));
//        userRepository.delete(user);
//    }

		
		
	
	
	

}