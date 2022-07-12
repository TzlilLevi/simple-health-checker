package com.tzlillevi.simplehealthchecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


@RestController
public class HealthCheckController {
    @Value("${healthcheck.target}")
    private String targetsConfig;

    private List<HealthCheckTarget> healthCheckTargets;

    @PostConstruct
    void init() {
        healthCheckTargets = new ArrayList<>();
        String[] targetStrings = targetsConfig.split(",");
        for(String targetStr : targetStrings){
            HealthCheckTarget target = getHealthCheckTarget(targetStr);
            healthCheckTargets.add(target);
        }
    }

    private final WebCallsService webCallsService;

    public HealthCheckController(WebCallsService webCallsService) {
        this.webCallsService = webCallsService;
    }

    @GetMapping("/health")
    public List<HealthResponse> getHealth(HttpServletResponse response) {
        boolean isHealthy = true;
        List<HealthResponse> healthResponses = new ArrayList<>();
        Map<String, Future<ResponseEntity<String>>> results = new HashMap<>();

        //call to health check targets
        for(HealthCheckTarget target : healthCheckTargets){
            Future<ResponseEntity<String>> result = callHealthCheckAsync(target.uri);
            results.put(target.name, result);
        }
        //collect responses
        for (var result : results.entrySet()) {
            HealthResponse resultOfHealthResponse = getHealthResponse(result.getKey(), result.getValue());
            healthResponses.add(resultOfHealthResponse);
        }

        //calculate response status
        for(HealthResponse HealthResponse : healthResponses){
            if (!HealthResponse.isHealthy()) {
                isHealthy = false;
                break;
            }
        }
        if (!isHealthy) {
            response.setStatus(503);
        } else {
            response.setStatus(200);
        }
        return healthResponses;
    }

    private HealthCheckTarget getHealthCheckTarget(String targetStrings) {
        String[] targetArr = targetStrings.split(";");
        String uri = targetArr[0];
        String name = targetArr[1];
        return new HealthCheckTarget(uri, name);
    }

    public Future<ResponseEntity<String>> callHealthCheckAsync(String uri) {
        return webCallsService.call(uri);
    }

    public HealthResponse getHealthResponse(String name, Future<ResponseEntity<String>> result) {
        int httpStatus = -1;
        String cause = null;
        boolean isHealthy;
        try {
            isHealthy = result.get().getStatusCode().is2xxSuccessful();
            httpStatus = result.get().getStatusCode().value();
        } catch (Exception e) {
            isHealthy = false;
            cause = e.getMessage();
        }
        return new HealthResponse(name, isHealthy, httpStatus, cause);
    }

    private class HealthCheckTarget {
        String uri;
        String name;

        public HealthCheckTarget(String uri, String name) {
            this.uri = uri;
            this.name = name;
        }
    }
}
