package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
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
    @Column(name = "id_user")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotBlank
    @Column(name = "first_name_user", nullable = false, length = 60)
    private String firstname;

    @NotBlank
    @Column(name = "last_name_user", nullable = false, length = 60)
    private String lastname;

    @NotBlank
    @Email
    @Column(name = "email_user", nullable = false, length = 80, unique = true)
    private String email;

    @NotBlank
    @Column(name = "password_user", nullable = false)
    private String password;

    @Column(name = "active_user", nullable = false)
    private boolean active;

    @Column(name = "create_at_user")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at_user")
    @UpdateTimestamp
    private Timestamp updateAt;

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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}