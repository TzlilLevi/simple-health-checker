package com.tzlillevi.simplehealthchecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
public class HealthCheckController {
    @Value("${healthcheck.target}")
    private String target;

    @Value("${healthcheck.time}")
    private int time;

    private final WebCallsService webCallsService;

    public HealthCheckController(WebCallsService webCallsService) {
        this.webCallsService = webCallsService;
    }

    @GetMapping("/health")
    public List<HealthResponse> getHealth(HttpServletResponse response) {
        boolean isHealthy = true;
        String[] listOfTargets = target.split(",");
        List<HealthResponse> listHealthResponse = new ArrayList<>();
        for (int i = 0; i < listOfTargets.length; i++) {
            HealthResponse targetResponse = getHealthResponse(listOfTargets[i]);
            listHealthResponse.add(targetResponse);
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

    public HealthResponse getHealthResponse(String target) {
        String[] targetArr = target.split(";");
        final String uri = targetArr[0];
        final String name = targetArr[1];
        int httpStatus = -1;
        String cause = null;
        boolean isHealthy;
        try {
            ResponseEntity<String> result = webCallsService.call(uri);
            isHealthy = result.getStatusCode().is2xxSuccessful();
            httpStatus = result.getStatusCode().value();
        } catch (Exception e) {
            isHealthy = false;
            cause = e.getMessage();
        }
        return new HealthResponse(name, isHealthy, httpStatus, cause);
    }
}
