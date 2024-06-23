package com.example.event_flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EventFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventFluxApplication.class, args);
	}

}
