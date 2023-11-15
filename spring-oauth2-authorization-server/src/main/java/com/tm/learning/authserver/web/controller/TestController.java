package com.tm.learning.authserver.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(path = "/")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Hello World");
    }
}
