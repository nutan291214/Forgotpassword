package com.example.springsocial.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.springsocial.model.Restaurant;
import com.example.springsocial.repository.RestaurantRepository;
import com.example.springsocial.service.RestaurantService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/zonions")
public class RestaurantController {
  @Autowired
  private RestaurantService restaurantService;
  @Autowired
  private RestaurantRepository restaurantRepository;

  private static final String RESTAURANT_SERVICE = "restaurantService";

  public ResponseEntity<String> rateLimiterFallback(Exception e) {
    return new ResponseEntity<>(
        "Too many requests - restaurant service does not permit further calls. Please retry after sometime",
        HttpStatus.TOO_MANY_REQUESTS);

  }

  // To add restaurant object
  @PostMapping("/restaurant")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Restaurant> save(@RequestBody Restaurant rest) {
    return restaurantService.createRestaurant(rest);
  }


  // To upload menu
  @PutMapping("/restaurantImage/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String menuUpload(@RequestParam MultipartFile file, @PathVariable int id) {
    return restaurantService.uploadImage(file, id);
  }

  // To get All restaurant
  @GetMapping("/restaurant")
  @RateLimiter(name = RESTAURANT_SERVICE, fallbackMethod = "rateLimiterFallback")
  public ResponseEntity<List<Restaurant>> getRestaurant() {
    List<Restaurant> list = restaurantService.getAllRestaurant();
    return ResponseEntity.of(Optional.of(list));
  }

  // To get all active rerstaurants
  @GetMapping("/restaurant/active")
  @RateLimiter(name = RESTAURANT_SERVICE, fallbackMethod = "rateLimiterFallback")
  public ResponseEntity<List<Restaurant>> getActiveRestaurants() {
    List<Restaurant> list = restaurantService.getActiveRestaurants();
    return ResponseEntity.of(Optional.of(list));
  }

  // To get restaurants By id
  @GetMapping("/restaurant/{id}")
  @RateLimiter(name = RESTAURANT_SERVICE, fallbackMethod = "rateLimiterFallback")
  public ResponseEntity<Restaurant> getById(@PathVariable int id) {
    return restaurantService.getRestaurantById(id);
  }

  // To get Restaurant by status
  @GetMapping("/restaurants/{status}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public Restaurant getByStatus(@PathVariable String status) {
    return restaurantService.getRestaurantByStatus(status);
  }

  // To delete restaurant By Id
  @DeleteMapping("/restaurant/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteByRestaurantId(@PathVariable int id) {
    return restaurantService.deleteById(id);
  }

  // To update Restaurant
  @PutMapping("/restaurant/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Restaurant> updateRestaurant(@PathVariable int id,
      @RequestBody Restaurant rm) {
    System.out.println("Inside update controller");
    return restaurantService.updateRestaurant(id, rm);
  }

  // To get Image By name and restaurant Id
  @GetMapping("/file/{name}/{id}")
  public ResponseEntity<byte[]> getFileByName(@PathVariable String name, @PathVariable int id) {
    System.out.println("name=" + name + "Id=" + id);
    Optional<Restaurant> fileOptional = restaurantRepository.findByNameAndId(name, id);

    if (fileOptional.isPresent()) {
      Restaurant file = fileOptional.get();
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + file.getName() + "\"").body(file.getPic());
    }

    return ResponseEntity.status(404).body(null);
  }

  // To change restaurant status active/deactive
  @PutMapping("/changestatus/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Restaurant statusChange(@PathVariable int id, @RequestBody Restaurant rm) {
    System.out.println("In change status of put....");
    return restaurantService.changeStatus(id, rm);
  }

}
