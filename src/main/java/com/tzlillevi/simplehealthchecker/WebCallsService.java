package com.tzlillevi.simplehealthchecker;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class WebCallsService {
     RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
    private SimpleClientHttpRequestFactory getClientHttpRequestFactory()
    {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout(5000);

        //Read timeout
        clientHttpRequestFactory.setReadTimeout(5000);
        return clientHttpRequestFactory;
    }

    public ResponseEntity<String> call(String uri) {
        return restTemplate.getForEntity(uri, String.class);
    }


}
