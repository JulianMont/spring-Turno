package com.oo2.grupo3.repositories;

import com.oo2.grupo3.models.entities.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IExampleRepository extends JpaRepository<Example, Integer> {

    //Search grupo3 with softDeleted = False
    Page<Example> findAllBySoftDeletedFalse(Pageable pageable);

    //Search grupo3 with softDeleted = False by ID
    Optional<Example> findByIdAndSoftDeletedFalse(Integer id);
}