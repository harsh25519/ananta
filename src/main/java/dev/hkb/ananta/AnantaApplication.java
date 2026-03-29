package dev.hkb.ananta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AnantaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnantaApplication.class, args);
	}

}
