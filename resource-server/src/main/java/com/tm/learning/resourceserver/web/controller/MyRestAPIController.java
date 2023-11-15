package com.tm.learning.resourceserver.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class MyRestAPIController {
    //GET Request to print a string

    @GetMapping(path = "/test")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Hello World");
    }
}
