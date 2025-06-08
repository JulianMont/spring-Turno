package com.oo2.grupo3;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oo2.grupo3.models.entities.RoleEntity;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.RoleType;
import com.oo2.grupo3.repositories.IRoleRepository;
import com.oo2.grupo3.repositories.IUserRepository;

@SpringBootTest
public class AdminCreationTest {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleRepository roleRepository; // Si tenés un repo para roles

    @Test
    void createAdminUser() {
        RoleEntity adminRole = roleRepository.findByType(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        UserEntity admin = UserEntity.builder()
                .firstname("Admin")
                .lastname("Principal")
                .email("admin@admin.com")
                .password(passwordEncoder.encode("admin123")) // 👈 Contraseña encriptada
                .active(true)
                .roleEntities(Set.of(adminRole))
                .build();

        userRepository.save(admin);
        System.out.println("✔ Admin creado: admin@admin.com / admin123");
    }
}
