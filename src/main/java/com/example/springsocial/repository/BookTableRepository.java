package com.example.springsocial.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.springsocial.model.BookTable;

public interface BookTableRepository extends CrudRepository<BookTable, Long> {

}
