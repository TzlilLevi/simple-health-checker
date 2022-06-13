package com.tzlillevi.simplehealthchecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
public class HealthCheckController {
    @Value("${healthcheck.target}")
    private String target;
    @Value("${server.port}")
    private Integer port;
    private final WebCallsService webCallsService;

    public HealthCheckController(WebCallsService webCallsService) {
        this.webCallsService = webCallsService;
    }

    @GetMapping("/health")
    public HealthResponse getHealth(HttpServletResponse response) {
        System.out.println(">>>>>>>>>>>>" + port);
        String[] targetArr = target.split(";");
        final String uri = targetArr[0];
        final String name = targetArr[1];
        int httpStatus = -1;
        String cause = null;
//        RestTemplate restTemplate = new RestTemplate();
        boolean isHealthy;
        try {
            ResponseEntity<String> result = webCallsService.call(uri);
//            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
            isHealthy = result.getStatusCode().is2xxSuccessful();
            httpStatus = result.getStatusCode().value();
        } catch (Exception e) {
            isHealthy = false;
            cause = e.getMessage();
        }
        if (!isHealthy) {
            response.setStatus(503);
        } else {
            response.setStatus(200);
        }
        return new HealthResponse(name, isHealthy, httpStatus, cause);
    }
}
