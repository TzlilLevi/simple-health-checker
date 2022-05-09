package com.tzlillevi.simplehealthchecker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@SpringBootApplication
@RestController

//@RequestMapping
public class HealthCheckController {

    @GetMapping("/health")
    public HealthResponse getHealth(HttpServletResponse response) {
        var random = new Random();
        var checkHealth = random.nextBoolean();
        if (!checkHealth) {
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
