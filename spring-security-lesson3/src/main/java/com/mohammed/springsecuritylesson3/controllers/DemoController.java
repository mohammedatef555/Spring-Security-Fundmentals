package com.mohammed.springsecuritylesson3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Demo!");
    }
}
