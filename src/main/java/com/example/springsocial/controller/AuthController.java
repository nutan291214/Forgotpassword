package com.example.springsocial.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.exception.UserNotVerifiedException;
import com.example.springsocial.model.ConfirmationToken;
import com.example.springsocial.model.User;
import com.example.springsocial.payload.ApiResponse;
import com.example.springsocial.payload.AuthResponse;
import com.example.springsocial.payload.LoginRequest;
import com.example.springsocial.payload.SignUpRequest;
import com.example.springsocial.payload.VerifyEmailRequest;
import com.example.springsocial.repository.RoleRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CustomUserDetailsService;
import com.example.springsocial.security.TokenProvider;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.AuthService;
import com.example.springsocial.service.EmailSenderService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private EmailSenderService emailSenderService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
      HttpServletRequest request) {

    System.out.println("inside login controller");
    UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());

    if (userDetailsService.isAccountVerified(user.getUsername()) == false) {
      throw new UserNotVerifiedException(user.getUsername() + " is not verified");
    }

    Authentication authentication =
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = tokenProvider.createToken(authentication);

    UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());
    List<String> role = new ArrayList<>();

    @SuppressWarnings("unchecked")
    List<String> sessionUser = (List<String>) request.getSession().getAttribute("SESSION_USER");
    if (sessionUser == null) {
      sessionUser = new ArrayList<>();

      request.getSession().setAttribute("SESSION_USER", loginRequest.getEmail());
      request.getSession().setMaxInactiveInterval(10 * 60);

    }

    sessionUser.add(loginRequest.getEmail());
    request.getSession().setAttribute("SESSION_USER", sessionUser);

    request.getSession().setMaxInactiveInterval(10 * 60);


    return ResponseEntity
        .ok(new AuthResponse(token, userDetails.getId(), userDetails.getUsername(), roles));

  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

    if (authService.existsByEmail(signUpRequest.getEmail())) {
      throw new BadRequestException("Account already exists on this mail Id.");
    }


    User user = authService.saveUser(signUpRequest);
    ConfirmationToken confirmationToken = authService.createToken(user);
    emailSenderService.sendMail(user.getEmail(), confirmationToken.getConfirmationToken());



    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
        .buildAndExpand(user.getId()).toUri();

    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "User registered successfully"));
  }

  @GetMapping("/confirm-account")
  public ResponseEntity<?> getMethodName(@RequestParam("token") String token) {

    ConfirmationToken confirmationToken = authService.findByConfirmationToken(token);

    if (confirmationToken == null) {
      throw new BadRequestException("Invalid token");
    }

    User user = confirmationToken.getUser();
    Calendar calendar = Calendar.getInstance();

    if ((confirmationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
      return ResponseEntity.badRequest()
          .body("Link expired. Generate new link from http://localhost:4200/login");
    }

    user.setEmailVerified(true);
    authService.save(user);
    return ResponseEntity.ok("Account verified successfully!");
  }

  @PostMapping("/send-email")
  public ResponseEntity<?> sendVerificationMail(
      @Valid @RequestBody VerifyEmailRequest emailRequest) {
    if (authService.existsByEmail(emailRequest.getEmail())) {
      if (userDetailsService.isAccountVerified(emailRequest.getEmail())) {
        throw new BadRequestException("Email is already verified");
      } else {
        User user = authService.findByEmail(emailRequest.getEmail());
        ConfirmationToken token = authService.createToken(user);
        emailSenderService.sendMail(user.getEmail(), token.getConfirmationToken());
        return ResponseEntity
            .ok(new ApiResponse(true, "Verification link is sent on your mail id"));
      }
    } else {
      throw new BadRequestException("Email is not associated with any account");
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody LoginRequest loginRequest) {
    if (authService.existsByEmail(loginRequest.getEmail())) {
      if (authService.changePassword(loginRequest.getEmail(), loginRequest.getPassword())) {
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
      } else {
        throw new BadRequestException("Unable to change password. Try again!");
      }
    } else {
      throw new BadRequestException("User not found with this email id");
    }
  }
}
