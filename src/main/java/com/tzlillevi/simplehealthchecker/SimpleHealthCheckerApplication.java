package com.tzlillevi.simplehealthchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class SimpleHealthCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleHealthCheckerApplication.class, args);

	}

}
