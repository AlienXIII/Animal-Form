package com.hoppy.hobbyProject.Repo;

import com.hoppy.hobbyProject.domain.Hobby;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HobbyRepository extends CrudRepository<Hobby, Long> {

    @Override
    List<Hobby> findAll();
}
