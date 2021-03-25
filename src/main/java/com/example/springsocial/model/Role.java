package com.example.springsocial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
public @Data class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Enumerated(EnumType.STRING)
  @Column(unique = true)
  private ERole name;

  public Role() {}

  public Role(ERole name) {
    this.name = name;
  }

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public ERole getName() {
	return name;
}

public void setName(ERole name) {
	this.name = name;
}
  


}
