package com.salmalteam.salmal.presentation.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/health")
    public String checkHealth(){
        return "healthy";
    }
}
