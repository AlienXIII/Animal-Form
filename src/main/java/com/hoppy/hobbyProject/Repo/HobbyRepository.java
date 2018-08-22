package com.hoppy.hobbyProject.Repo;

import com.hoppy.hobbyProject.domain.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {

    @Override
    List<Hobby> findAll();
}
