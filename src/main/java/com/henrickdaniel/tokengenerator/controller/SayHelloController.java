package com.henrickdaniel.tokengenerator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHelloController {

    @PostMapping("/say-hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello World");
    }

}
