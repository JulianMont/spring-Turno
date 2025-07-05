package com.oo2.grupo3.configurations.seeder;

import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Especialidad;
import com.oo2.grupo3.models.entities.RoleEntity;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.RoleType;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IEspecialidadRepository;
import com.oo2.grupo3.repositories.IRoleRepository;
import com.oo2.grupo3.repositories.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class UsersSeeder implements CommandLineRunner {

    private static final String passwordGeneric = "foo1234";

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IEspecialidadRepository especialidadRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IClienteRepository clienteRepository;

    public UsersSeeder(IUserRepository userRepository,
                       IRoleRepository roleRepository,
                       IEspecialidadRepository especialidadRepository,
                       IEmpleadoRepository empleadoRepository,
                       IClienteRepository clienteRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.especialidadRepository = especialidadRepository;
        this.empleadoRepository = empleadoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadEspecialidad();
        loadUsers();
    }

    private void loadRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(buildRole(RoleType.USER));
            roleRepository.save(buildRole(RoleType.ADMIN));
        }
    }

    private RoleEntity buildRole(RoleType roleType) {
        return RoleEntity.builder()
                .type(roleType)
                .build();
    }

    private void loadEspecialidad() {
        if (especialidadRepository.count() == 0) {
            especialidadRepository.save(buildEspecialidad("EspecialidadTest"));
        }
    }

    private Especialidad buildEspecialidad(String nombre) {
        return Especialidad.builder()
                .nombre(nombre)
                .build();
    }

    private void loadUsers() {
        if (userRepository.count() == 0) {
            loadUser();
            loadUserAdmin();
        }
    }

    private void loadUser() {
    	
        int dniClienteUser = 87654321;

        Optional<Cliente> optionalCliente = clienteRepository.findByDni(dniClienteUser);
        Cliente clienteUser;

        if (optionalCliente.isPresent()) {
            clienteUser = optionalCliente.get();
        } else {
            Cliente c = Cliente.builder()
                    .nombre("Profesor")
                    .apellido("UNLaUser")
                    .dni(dniClienteUser)
                    .build();
            clienteUser = clienteRepository.save(c);
        }
    	
        userRepository.save(buildUserUser(clienteUser,"user@hotmail.com", passwordGeneric));
    }

    private UserEntity buildUserUser(Cliente clienteUser,String email, String password) {
        return UserEntity.builder()
        		.persona(clienteUser)
                .email(email)
                .password(encryptPassword(password))
                .roleEntities(Set.of(roleRepository.findByType(RoleType.USER).get()))
                .build();
    }

    private void loadUserAdmin() {
        Especialidad especialidad = especialidadRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay especialidad cargada en la base de datos"));

        int dniAdmin = 12345678;

        Empleado empleadoAdmin;
        var empleadoOptional = empleadoRepository.findByDni(dniAdmin);

        if (empleadoOptional.isPresent()) {
            empleadoAdmin = empleadoOptional.get();
        } else {
            empleadoAdmin = Empleado.builder()
                    .nombre("Profesor")
                    .apellido("UNLaAdmin")
                    .dni(dniAdmin)
                    .legajo("999")
                    .especialidad(especialidad)
                    .build();

            empleadoAdmin = empleadoRepository.save(empleadoAdmin);
        }

        UserEntity adminUser = buildUserAdmin(empleadoAdmin, "admin@gmail.com", passwordGeneric);
        userRepository.save(adminUser);
    }


    private UserEntity buildUserAdmin(Empleado empleadoAdmin, String email, String password) {
        return UserEntity.builder()
                .persona(empleadoAdmin)
                .email(email)
                .password(encryptPassword(password))
                .roleEntities(Set.of(roleRepository.findByType(RoleType.ADMIN).get()))
                .build();
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
        return passwordEncoder.encode(password);
    }
}
