package com.example.springsocial.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springsocial.dto.UserDto;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.CustomUserDetailsService;
import com.example.springsocial.security.UserPrincipal;

@RestController
@RequestMapping("/zonions")
@CrossOrigin(origins = "*")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  CustomUserDetailsService service;

  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
    System.out.println("Inside user controller");
    return userRepository.findById(userPrincipal.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
  }

  @GetMapping("/users")
  public List<UserDto> getAll() {
    System.out.println(service.getAllUsers());
    return service.getAllUsers();
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
    return service.getUserById(id);
  }

  @PutMapping("/users/{id}")
  public UserDto changeRole(@PathVariable long id, @RequestBody User user) {
    return service.changeRole(id, user);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<HttpStatus> deleteById(@PathVariable long id) {

    return service.deleteById(id);

  }
}
