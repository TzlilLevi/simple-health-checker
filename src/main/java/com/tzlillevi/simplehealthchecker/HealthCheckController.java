package com.tzlillevi.simplehealthchecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@SpringBootApplication
@RestController

//@RequestMapping
public class HealthCheckController {
    @Value("${healthcheck.target}")
    private String target;
    @GetMapping("/health")
    public HealthResponse getHealth(HttpServletResponse response) {

        final String uri= target;
        RestTemplate restTemplate = new RestTemplate();
        boolean isHealthy;
        try {
            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
            isHealthy = result.getStatusCode().is2xxSuccessful();
        }
        catch(Exception e){
            isHealthy = false;
        }


        if (!isHealthy) {
            response.setStatus(503);
            System.out.println("ERROR: 503");
            return new HealthResponse("BAD");
        } else {
            response.setStatus(200);
            System.out.println("OK: 200");
            return new HealthResponse("GOOD");
        }
    }
}
