package com.ehizman.auth_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class AuthAppApplication {

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
		SpringApplication.run(AuthAppApplication.class, args);
	}

}
