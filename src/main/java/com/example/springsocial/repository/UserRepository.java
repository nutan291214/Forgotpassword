package com.example.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.springsocial.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  Boolean existsByEmail(String email);

  Boolean deleteByEmail(String email);

  @Query("SELECT u.emailVerified FROM User u WHERE u.email = ?1")
  Boolean findEmailVerifiedByEmail(String email);
}
