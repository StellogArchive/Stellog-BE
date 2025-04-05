package org.example.stellog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class StellogApplication {

	public static void main(String[] args) {
		SpringApplication.run(StellogApplication.class, args);
	}

}
