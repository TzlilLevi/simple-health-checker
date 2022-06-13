package com.tzlillevi.simplehealthchecker;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebCallsService {
    private RestTemplate restTemplate = new RestTemplate();
    public ResponseEntity<String> call(String uri){
        return restTemplate.getForEntity(uri, String.class);
    }



}
