package com.tzlillevi.simplehealthchecker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class WebCallsService {
    final RestTemplate restTemplate;

    public WebCallsService(@Value("${healthcheck.time}") int time) {
        restTemplate = new RestTemplate(getClientHttpRequestFactory(time));
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory(int time) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout(time);

        //Read timeout
        clientHttpRequestFactory.setReadTimeout(time);
        return clientHttpRequestFactory;
    }

    public ResponseEntity<String> call(String uri) {
        return restTemplate.getForEntity(uri, String.class);
    }

}
