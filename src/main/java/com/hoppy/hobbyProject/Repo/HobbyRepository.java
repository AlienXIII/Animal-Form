package com.hoppy.hobbyProject.Repo;

import com.hoppy.hobbyProject.domain.Category;
import com.hoppy.hobbyProject.domain.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HobbyRepository extends JpaRepository<Hobby, Long> {

    @Override
    List<Hobby> findAll();

    @Query("SELECT SIZE(h.fileNames) FROM Hobby h WHERE h.id=:id")
    long countFilesByHobbyId(@Param("id")long id);

    @Query("SELECT h FROM Hobby h WHERE h.category=:category")  //to jest syntax mysql? poprostu SQL
    List<Hobby> findAllByCategory(@Param("category") Category category);
}

