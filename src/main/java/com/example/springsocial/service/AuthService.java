package com.example.springsocial.service;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.springsocial.model.AuthProvider;
import com.example.springsocial.model.ConfirmationToken;
import com.example.springsocial.model.ERole;
import com.example.springsocial.model.Role;
import com.example.springsocial.model.User;
import com.example.springsocial.payload.SignUpRequest;
import com.example.springsocial.repository.ConfirmationTokenRepository;
import com.example.springsocial.repository.RoleRepository;
import com.example.springsocial.repository.UserRepository;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ConfirmationTokenRepository confirmationTokenRepository;

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public User saveUser(SignUpRequest signUpRequest) {
    User user = new User();
    user.setName(signUpRequest.getName());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(signUpRequest.getPassword());
    user.setProvider(AuthProvider.local);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    final HashSet<Role> roles = new HashSet<Role>();
    roles.add(roleRepository.findByName(ERole.ROLE_USER));
    user.setRoles(roles);
    return userRepository.save(user);
  }

  public boolean changePassword(String email, String password) {
    User user = findByEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    if (save(user) != null) {
      return true;
    }
    return false;
  }

  public ConfirmationToken createToken(User user) {
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    return confirmationTokenRepository.save(confirmationToken);
  }

  public ConfirmationToken findByConfirmationToken(String token) {
    return confirmationTokenRepository.findByConfirmationToken(token);
  }

  public void deleteToken(ConfirmationToken confirmationToken) {
    this.confirmationTokenRepository.delete(confirmationToken);
  }
}
