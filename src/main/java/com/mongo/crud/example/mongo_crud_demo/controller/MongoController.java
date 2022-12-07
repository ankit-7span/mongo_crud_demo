package com.mongo.crud.example.mongo_crud_demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongo.crud.example.mongo_crud_demo.mappers.BookMapper;
import com.mongo.crud.example.mongo_crud_demo.models.Book;
import com.mongo.crud.example.mongo_crud_demo.repo.BookRepo;
import com.mongo.crud.example.mongo_crud_demo.request.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crud/")
public class MongoController {

    @Autowired
    WebClient.Builder webClient;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepo bookRepo;


    //WebClient
    @GetMapping("/getData")
    public String getExternalData() {
        Gson gson = new Gson();
        Object block = webClient.build()
                .get()
                .uri("https://www.boredapi.com/api/activity")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.headers(httpHeaders -> {}) For multiple haders
                .retrieve()
                .bodyToMono(Object.class).block();
        JsonObject jsonObject = gson.fromJson(gson.toJson(block), JsonObject.class);
        return String.valueOf(jsonObject.get("activity"));
    }

    //WebClient Post For List of request body and response
   /* @PostMapping("/addData")
    public List<String> addDataToWebClient(@RequestBody List<BookRequest> bookRequest) {
        return webClient.build().post()
                .uri("http://localhost:8080/api/v1/crud/add")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.headers(httpHeaders -> {}) For multiple haders
                .body(Mono.just(bookRequest), new ParameterizedTypeReference<List<BookRequest>>() {
                })
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                }).block();
    }*/

    //WebClient
    @PutMapping("/updateData/{id}")
    public String updateDataToWebClient(@PathVariable(name = "id") int id, @RequestBody BookRequest bookRequest) {
        return webClient.build().put()
                .uri("http://localhost:8080/api/v1/crud/update/" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.headers(httpHeaders -> {}) For multiple haders
                .body(Mono.just(bookRequest), BookRequest.class)
                .retrieve()
                .bodyToMono(String.class).block();
    }

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
        Book update = bookMapper.update(book1, bookRequest);
        bookRepo.save(update);
        return ResponseEntity.ok("Book Updated Successfully!");
    }


}
