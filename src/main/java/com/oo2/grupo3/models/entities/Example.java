package com.oo2.grupo3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "grupo3_abm")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Example {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo3_abm")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotBlank(message = "The name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "soft_deleted", nullable = false)
    @Builder.Default
    private boolean softDeleted = false;

    @Column(name = "create_at")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private Timestamp updateAt;

    //Uno a Uno --> https://www.baeldung.com/jpa-one-to-one
    //Uno a Muchos --> https://www.baeldung.com/hibernate-one-to-many
    //Muchos a Muchos --> https://www.baeldung.com/jpa-many-to-many
    //Herencia --> https://www.baeldung.com/hibernate-inheritance

    //Email --> https://www.baeldung.com/spring-email
    // o video tutorial --> https://www.youtube.com/watch?v=JKmzV1MY_-M&t=91s&ab_channel=UnProgramadorNace
}
