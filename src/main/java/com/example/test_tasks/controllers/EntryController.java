package com.example.test_tasks.controllers;

import com.example.test_tasks.entites.dtos.requests.LogInRequest;
import com.example.test_tasks.entites.dtos.requests.SignInRequest;
import com.example.test_tasks.controllers.services.EntryService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entry")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntryController {

    EntryService homeService;

    @PostMapping("/sign")
    public ResponseEntity<Void> sign(@RequestBody SignInRequest dto){

       return homeService.sign(dto);
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LogInRequest dto){

        return homeService.login(dto);
    }

    @PostMapping("/pending-verify")
    public ResponseEntity<Void> pending(@RequestParam String email){

        return homeService.pending_verify(email);
    }

    @PostMapping("/verify/{code}")
    public ResponseEntity<Void> verify(@PathVariable String code){

        return homeService.verify(code);
    }





}
