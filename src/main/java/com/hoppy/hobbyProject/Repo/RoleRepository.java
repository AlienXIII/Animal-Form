package com.hoppy.hobbyProject.Repo;

import com.hoppy.hobbyProject.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}