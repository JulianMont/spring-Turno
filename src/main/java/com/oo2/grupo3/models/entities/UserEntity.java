package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_user")
    private Persona persona;

    @NotBlank
    @Email
    @Column(name = "email_user", nullable = false, length = 80, unique = true)
    private String email;

    @NotBlank
    @Column(name = "password_user", nullable = false)
    private String password;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<RoleEntity> roleEntities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoleEntities().stream()
                .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getType().getPrefixedName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    
    
}