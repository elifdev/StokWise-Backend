package com.tobeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StokwiseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StokwiseBackendApplication.class, args);
	}

}
