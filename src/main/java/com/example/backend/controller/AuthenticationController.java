package com.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class AuthenticationController {
    @GetMapping()
    public List<String> test() {
        return List.of("Test is success, congratulations!");
    }
}
