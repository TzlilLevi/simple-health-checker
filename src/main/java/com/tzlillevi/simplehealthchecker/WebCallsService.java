package com.tzlillevi.simplehealthchecker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.Future;


@Service
public class WebCallsService {
    final AsyncRestTemplate asyncRestTemplate;

    public WebCallsService(@Value("${healthcheck.time}") int time) {
        asyncRestTemplate = new AsyncRestTemplate(getClientHttpRequestFactory(time));
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory(int time) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout
        clientHttpRequestFactory.setConnectTimeout(time);
        clientHttpRequestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());

        //Read timeout
        clientHttpRequestFactory.setReadTimeout(time);
        return clientHttpRequestFactory;
    }

    public Future<ResponseEntity<String>> call(String uri) {
        return asyncRestTemplate.getForEntity(uri, String.class);
    }

}
