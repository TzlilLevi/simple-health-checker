package com.tzlillevi.simplehealthchecker;

import org.springframework.http.ResponseEntity;

public class HealthResponse {
    private String name;
    private boolean healthy;
    private int http_status;
    private String cause;

    public HealthResponse(String name, boolean healthy, int http_status, String cause) {
        this.name = name;
        this.healthy = healthy;
        this.http_status = http_status;
        this.cause = cause;
    }

    public String getName() {
        return name;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public int getHttp_status() {
        return http_status;
    }

    public String getCause() {
        return cause;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setHttp_status(int http_status) {
        this.http_status = http_status;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
