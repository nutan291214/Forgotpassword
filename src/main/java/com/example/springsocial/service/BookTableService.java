package com.example.springsocial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springsocial.model.BookTable;
import com.example.springsocial.repository.BookTableRepository;

@Service
public class BookTableService {

  @Autowired
  BookTableRepository bookRepo;


  public BookTable bookTable(BookTable bookTable) {
    return bookRepo.save(bookTable);
  }
}
