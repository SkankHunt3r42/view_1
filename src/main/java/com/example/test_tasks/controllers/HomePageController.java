package com.example.test_tasks.controllers;

import com.example.test_tasks.controllers.services.BookService;
import com.example.test_tasks.entites.BookEntity;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomePageController {

    BookService bookService;

    @GetMapping("/history")
    public ResponseEntity<List<BookEntity>> viewMyBooks(@RequestParam(required = false) Boolean current){

        return bookService.viewMyBooks(current);
    }

    @GetMapping("/find")
    public ResponseEntity<BookEntity> findBook(@RequestParam String name){
        return bookService.findBookByName(name);
    }

    
}
