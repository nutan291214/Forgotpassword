package com.example.springsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.springsocial.model.BookTable;
import com.example.springsocial.service.BookTableService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/zonions")
public class BookTableController {

  @Autowired
  BookTableService bookTableService;

  @PostMapping("/bookTable")
  public BookTable bookTable(@RequestBody BookTable bookTable) {
    return bookTableService.bookTable(bookTable);
  }

}


