package com.example.springsocial.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
public @Data class Restaurant implements Serializable {

  private static final long serialVersionUID = 6665821165040459474L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "restaurant_name")
  private String restaurantName;
  @Column(name = "address")
  private String address;
  @Column(name = "phone_no")
  private String phoneNo;
  @Column(name = "open_time")
  private String open_time;
  @Column(name = "close_time")
  private String close_time;
  @Column(name = "status")
  private String status;
  @Column(name = "updated_time")
  private String updatedTime;
  // for image upload
  @Column(name = "name")
  @JsonView(View.FileInfo.class)
  private String name;

  @Column(name = "mimetype")
  private String mimetype;

  @Lob
  @Column(name = "pic")
  private byte[] pic;


  public Restaurant(String restaurantName, String address, String phoneNo, String open_time,
      String close_time, String status, String updatedTime) {
    this.restaurantName = restaurantName;
    this.address = address;
    this.phoneNo = phoneNo;
    this.open_time = open_time;
    this.close_time = close_time;
    this.status = status;
    this.updatedTime = updatedTime;

  }

  public Restaurant(String originalFilename, String contentType, byte[] bytes) {
    this.name = originalFilename;
    this.mimetype = contentType;
    this.pic = bytes;
  }

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getRestaurantName() {
	return restaurantName;
}

public void setRestaurantName(String restaurantName) {
	this.restaurantName = restaurantName;
}

public String getAddress() {
	return address;
}

public void setAddress(String address) {
	this.address = address;
}

public String getPhoneNo() {
	return phoneNo;
}

public void setPhoneNo(String phoneNo) {
	this.phoneNo = phoneNo;
}

public String getOpen_time() {
	return open_time;
}

public void setOpen_time(String open_time) {
	this.open_time = open_time;
}

public String getClose_time() {
	return close_time;
}

public void setClose_time(String close_time) {
	this.close_time = close_time;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getUpdatedTime() {
	return updatedTime;
}

public void setUpdatedTime(String updatedTime) {
	this.updatedTime = updatedTime;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getMimetype() {
	return mimetype;
}

public void setMimetype(String mimetype) {
	this.mimetype = mimetype;
}

public byte[] getPic() {
	return pic;
}

public void setPic(byte[] pic) {
	this.pic = pic;
}

  

}
