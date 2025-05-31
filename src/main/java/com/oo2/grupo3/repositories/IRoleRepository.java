package com.oo2.grupo3.repositories;

import com.oo2.grupo3.models.entities.RoleEntity;
import com.oo2.grupo3.models.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findById(Integer integer);

    Optional<RoleEntity> findByType(RoleType type);
}