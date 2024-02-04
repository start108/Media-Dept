package com.jy.hessed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HessedApplication {

	public static void main(String[] args) {
		SpringApplication.run(HessedApplication.class, args);
	}
}
