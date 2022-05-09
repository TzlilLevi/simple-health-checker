package com.tzlillevi.simplehealthchecker;

import org.springframework.http.ResponseEntity;

public class HealthResponse {
    private String message;
    public HealthResponse(String message) {
        this.message = message;
    }

    public String getResponse(){
        return message;
    }

}
