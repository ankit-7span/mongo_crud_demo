package com.mongo.crud.example.mongo_crud_demo.repo;

import com.mongo.crud.example.mongo_crud_demo.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends MongoRepository<Book, Integer> {



}
