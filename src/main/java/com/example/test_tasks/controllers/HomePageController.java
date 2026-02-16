package com.example.test_tasks.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomePageController {



    @GetMapping("/greetings")
    public ResponseEntity<String> greetings(){


        return ResponseEntity.ok("Greetings from secured");
    }
}
