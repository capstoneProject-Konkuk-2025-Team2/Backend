package com.capstone.backend.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthCheckController {
    @GetMapping("/")
    public String healthCheck() {
        return "capstone-Backend(Spring)";
    }
}
