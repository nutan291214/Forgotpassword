package com.example.springsocial.security;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.springsocial.dto.UserDto;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.ERole;
import com.example.springsocial.model.Role;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.RoleRepository;
import com.example.springsocial.repository.UserDtoRepository;
import com.example.springsocial.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserDtoRepository userDtoRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);


    return UserPrincipal.create(user);
  }


  private List<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    for (Role rolesname : roles) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + rolesname.getName()));
    }

    return authorities;
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    return UserPrincipal.create(user);
  }

  public List<UserDto> getAllUsers() {
    List<UserDto> list1 = userDtoRepository.findAll();
    return list1;
  }

  public ResponseEntity<UserDto> getUserById(Long id) {

    Optional<UserDto> user = userDtoRepository.findById(id);
    if (user.isPresent()) {
      return new ResponseEntity<UserDto>(user.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public UserDto changeRole(@PathVariable Long id, @RequestBody User user) {
    String st = "ROLE_ADMIN";
    String st1 = "ROLE_USER";
    Optional<UserDto> restdata = userDtoRepository.findById(id);

    UserDto entity = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRoles());

    if (restdata.isPresent()) {

      if (!user.getRoles().isEmpty()) {

        Set<Role> role = new HashSet<Role>();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        role.add(adminRole);
        entity.setRoles(role);

      }

    }

    return userDtoRepository.save(entity);
  }

  public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
    try {

      userRepository.deleteById(id);
      userDtoRepository.deleteById(id);

      return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  public boolean isAccountVerified(String email) {
    boolean isVerified = userRepository.findEmailVerifiedByEmail(email);
    return isVerified;
  }
}
