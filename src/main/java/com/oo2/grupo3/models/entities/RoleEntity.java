package com.oo2.grupo3.models.entities;

import com.oo2.grupo3.models.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name_role", nullable = false, length = 80, unique = true)
    private RoleType type;

    @Column(name = "create_at_role")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at_role")
    @UpdateTimestamp
    private Timestamp updateAt;

    public RoleEntity(@NotNull RoleType type){
        this.type = type;
    }
}