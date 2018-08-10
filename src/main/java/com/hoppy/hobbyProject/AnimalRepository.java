package com.hoppy.hobbyProject;

import com.hoppy.hobbyProject.domain.Animal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnimalRepository extends CrudRepository<Animal, Long> {

    @Override
    List<Animal> findAll();
}
