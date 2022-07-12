package com.tzlillevi.simplehealthchecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


@RestController
public class HealthCheckController {
    @Value("${healthcheck.target}")
    private String target;

    private final WebCallsService webCallsService;

    public HealthCheckController(WebCallsService webCallsService) {
        this.webCallsService = webCallsService;
    }

    @GetMapping("/health")
    public List<HealthResponse> getHealth(HttpServletResponse response) {
        boolean isHealthy = true;
        String[] targetStrings = target.split(",");
        List<HealthResponse> listHealthResponse = new ArrayList<>();
        Map<String, Future<ResponseEntity<String>>> results = new HashMap<>();

        for (int i = 0; i < targetStrings.length; i++) {
            String[] targetArr = targetStrings[i].split(";");
            final String uri = targetArr[0];
            String name = targetArr[1];
            Future<ResponseEntity<String>> result = callHealthCheckAsync(uri);
            if (!(result == null)) {
                results.put(name, result);
            }
        }

        for (var result : results.entrySet()) {
            HealthResponse resultOfHealthResponse = getHealthResponse(result.getKey(), result.getValue());
            listHealthResponse.add(resultOfHealthResponse);
        }

        for (int i = 0; i < listHealthResponse.size(); i++) {
            if (!listHealthResponse.get(i).isHealthy()) {
                isHealthy = false;
                break;
            }
        }
        if (!isHealthy) {
            response.setStatus(503);
        } else {
            response.setStatus(200);
        }
        return listHealthResponse;
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
}
