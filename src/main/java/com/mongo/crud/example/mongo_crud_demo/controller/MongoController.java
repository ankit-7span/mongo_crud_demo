package com.mongo.crud.example.mongo_crud_demo.controller;

import com.mongo.crud.example.mongo_crud_demo.mappers.BookMapper;
import com.mongo.crud.example.mongo_crud_demo.models.Book;
import com.mongo.crud.example.mongo_crud_demo.repo.BookRepo;
import com.mongo.crud.example.mongo_crud_demo.request.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/crud/")
public class MongoController {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable(name = "id") int id) {
        bookRepo.deleteById(id);
        return ResponseEntity.ok("Book Deleted Successfully!");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody BookRequest book) {
        Book book1 = bookMapper.toEnity(book);
        bookRepo.save(book1);
        return ResponseEntity.ok("Book Added Successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable(name = "id") int id, @RequestBody BookRequest bookRequest) {
        Book book1 = bookRepo.findById(id).orElseThrow(RuntimeException::new);
        bookMapper.update(book1,bookRequest);
        return ResponseEntity.ok("Book Updated Successfully!");
    }


}
