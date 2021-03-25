package com.example.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springsocial.model.ERole;
import com.example.springsocial.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Role findByName(ERole name);

  boolean existsByName(ERole name);
}
