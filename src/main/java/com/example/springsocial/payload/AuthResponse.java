package com.example.springsocial.payload;

import java.util.List;
import lombok.Data;

public @Data class AuthResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private Long id;
  private String email;
  private List<String> roles;

  public AuthResponse(String accessToken, Long id, String email, List<String> roles) {
    this.accessToken = accessToken;
    this.id = id;
    this.email = email;
    this.roles = roles;
  }

public String getAccessToken() {
	return accessToken;
}

public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
}

public String getTokenType() {
	return tokenType;
}

public void setTokenType(String tokenType) {
	this.tokenType = tokenType;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public List<String> getRoles() {
	return roles;
}

public void setRoles(List<String> roles) {
	this.roles = roles;
}
  
}
