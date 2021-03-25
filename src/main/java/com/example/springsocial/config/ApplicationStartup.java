package com.example.springsocial.config;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.springsocial.dto.UserDto;
import com.example.springsocial.model.AuthProvider;
import com.example.springsocial.model.ERole;
import com.example.springsocial.model.Role;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.RoleRepository;
import com.example.springsocial.repository.UserDtoRepository;
import com.example.springsocial.repository.UserRepository;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
  private static boolean alreadySetup = false;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserDtoRepository userDtoRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }

    Role adminRole = createRoleIfNotFound(ERole.ROLE_ADMIN);
    Role userRole = createRoleIfNotFound(ERole.ROLE_USER);
    createUserIfNotFound("zonionsapp@gmail.com", Set.of(adminRole));
    createUserDtoIfNotFound("zonionsapp@gmail.com", Set.of(adminRole));
    alreadySetup = true;

  }

  @Transactional
  private final User createUserIfNotFound(final String email, Set<Role> roles) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      user = new User();
      user.setName("Admin");
      user.setEmail(email);
      user.setEmailVerified(true);
      user.setPassword(passwordEncoder.encode("Admin@123"));
      user.setRoles(roles);
      user.setProvider(AuthProvider.local);
      user = userRepository.save(user);
    }
    return user;
  }

  @Transactional
  private final UserDto createUserDtoIfNotFound(final String email, Set<Role> roles) {
    UserDto user = userDtoRepository.findByEmail(email);
    if (user == null) {
      user = new UserDto();
      user.setName("Admin");
      user.setEmail(email);
      user.setRoles(roles);
      user = userDtoRepository.save(user);
    }
    return user;
  }

  @Transactional
  private final Role createRoleIfNotFound(final ERole name) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      role = roleRepository.save(new Role(name));
    }

    return role;
  }
}

