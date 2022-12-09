package com.mongo.crud.example.mongo_crud_demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongo.crud.example.mongo_crud_demo.configuration.CustomConfiguration;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/crud/")
public class MongoController {

    @Autowired
    CustomConfiguration configuration;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepo bookRepo;

    WebClient webClient;

    @Autowired
    public MongoController(CustomConfiguration configuration) {
        this.webClient = configuration.getWebClient();
    }

    //WebClient
    @GetMapping("/getData/{pin}")
    public Map<String, String> getExternalData(@PathVariable(name = "pin")String pin) {
        Gson gson = new Gson();
        Object block = webClient
                .get()
                .uri("https://viacep.com.br/ws/"+pin+"/json/")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.headers(httpHeaders -> {}) For multiple haders
                .retrieve()
                .bodyToMono(Object.class).block();
        JsonObject jsonObject = gson.fromJson(gson.toJson(block), JsonObject.class);
        Map<String,String> address=new HashMap<>();
        address.put("State",jsonObject.get("uf").getAsString());
        address.put("City",jsonObject.get("localidade").getAsString());
        address.put("Street",jsonObject.get("logradouro").getAsString());
        address.put("Neighborhood",jsonObject.get("bairro").getAsString());
        address.put("Zip",jsonObject.get("cep").getAsString());
        return address;
    }

    //WebClient Post For List of request body and response
   /* @PostMapping("/addData")
    public List<String> addDataToWebClient(@RequestBody List<BookRequest> bookRequest) {
        return webClient.post()
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
        return webClient.put()
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
    public ResponseEntity<String> addBook(@RequestBody BookRequest bookRequest) {
        Book book = bookMapper.toEnity(bookRequest);
        bookRepo.save(book);
        return ResponseEntity.ok("Book Added Successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable(name = "id") int id, @RequestBody BookRequest bookRequest) {
        Book book = bookRepo.findById(id).orElseThrow(RuntimeException::new);
        Book updatedBook = bookMapper.update(book, bookRequest);
        bookRepo.save(updatedBook);
        return ResponseEntity.ok("Book Updated Successfully!");
    }


}
