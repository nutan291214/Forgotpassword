package com.example.springsocial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public @Data class BookTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private int noOfSeats;

  @Column
  private String date;

  @Column
  private String bookTime;

  @Column
  private String restaurantName;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public int getNoOfSeats() {
	return noOfSeats;
}

public void setNoOfSeats(int noOfSeats) {
	this.noOfSeats = noOfSeats;
}

public String getDate() {
	return date;
}

public void setDate(String date) {
	this.date = date;
}

public String getBookTime() {
	return bookTime;
}

public void setBookTime(String bookTime) {
	this.bookTime = bookTime;
}

public String getRestaurantName() {
	return restaurantName;
}

public void setRestaurantName(String restaurantName) {
	this.restaurantName = restaurantName;
}

  
}
