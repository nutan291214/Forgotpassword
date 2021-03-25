package com.example.springsocial.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.springsocial.model.Restaurant;
import com.example.springsocial.repository.RestaurantRepository;

@Service
public class RestaurantService {
  @Autowired
  private RestaurantRepository restaurantRepository;
  String status = "open";
  LocalTime time = LocalTime.now();
  String updatedTime = time.toString();

  // Service To add restaurant object
  public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant rm) {
    try {

      Restaurant _restaurant =
          restaurantRepository.save(new Restaurant(rm.getRestaurantName(), rm.getAddress(),
              rm.getPhoneNo(), rm.getOpen_time(), rm.getClose_time(), status, updatedTime));
      return new ResponseEntity<>(_restaurant, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Service To add Image
  public String uploadImage(@RequestParam("file") MultipartFile file, @PathVariable int id) {

    Optional<Restaurant> restData = restaurantRepository.findById(id);
    try {
      if (restData.isPresent()) {
        Restaurant restModel = restData.get();
        restModel.setName(file.getOriginalFilename());
        restModel.setMimetype(file.getContentType());
        restModel.setPic(file.getBytes());

        restaurantRepository.save(restModel);

      }
      return "File uploaded successfully! -> filename = " + file.getOriginalFilename();
    } catch (Exception e) {
      return "FAIL! Maybe You had uploaded the file before or the file's size > 500KB";
    }

  }

  // Service to get all restaurants
  public List<Restaurant> getAllRestaurant() {
    return restaurantRepository.findAll();
  }

  // Service to get all active restaurants
  public List<Restaurant> getActiveRestaurants() {
    List<Restaurant> list = restaurantRepository.findAll();
    List<Restaurant> activeRestaurantList = new ArrayList<Restaurant>();
    for (Restaurant list1 : list) {
      if (list1.getStatus().equalsIgnoreCase("open")) {
        activeRestaurantList.add(list1);
      }
    }
    return activeRestaurantList;
  }

  // Service to get restaurant by id
  public ResponseEntity<Restaurant> getRestaurantById(@PathVariable int id) {
    Optional<Restaurant> restaurant = restaurantRepository.findById(id);
    if (restaurant.isPresent()) {
      return new ResponseEntity<Restaurant>(restaurant.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // Service to get restaurant
  public Restaurant getRestaurantByStatus(@PathVariable String status) {
    Restaurant data = restaurantRepository.findByStatus(status);
    return data;
  }

  public ResponseEntity<HttpStatus> deleteById(@PathVariable int id) {
    try {
      restaurantRepository.deleteById(id);
      return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Restaurant> updateRestaurant(@PathVariable int id,
      @RequestBody Restaurant resmodel) {
    Optional<Restaurant> restData = restaurantRepository.findById(id);
    if (restData.isPresent()) {
      Restaurant rs = restData.get();
      rs.setRestaurantName(resmodel.getRestaurantName());
      rs.setAddress(resmodel.getAddress());
      rs.setPhoneNo(resmodel.getPhoneNo());
      rs.setOpen_time(resmodel.getOpen_time());
      rs.setClose_time(resmodel.getClose_time());
      rs.setStatus(resmodel.getStatus());
      rs.setUpdatedTime(updatedTime);
      return new ResponseEntity<Restaurant>(restaurantRepository.save(rs), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public Restaurant changeStatus(@PathVariable int id, @PathVariable Restaurant resm) {
    System.out.println(resm);
    String st = "closed";
    String st1 = "open";

    Optional<Restaurant> restdata = restaurantRepository.findById(id);

    Restaurant rm = restdata.get();

    if (restdata.isPresent()) {

      if (resm.getStatus().equals("open")) {
        rm.setStatus(st);

      } else {
        rm.setStatus(st1);

      }
    }

    return restaurantRepository.save(rm);
  }

}
