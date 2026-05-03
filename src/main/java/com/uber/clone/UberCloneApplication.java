package com.uber.clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UberCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberCloneApplication.class, args);
	}
}