package com.example.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springsocial.dto.UserDto;

@Repository
public interface UserDtoRepository extends JpaRepository<UserDto, Long> {

  UserDto findByEmail(String email);

  Boolean deleteByEmail(String email);
}
