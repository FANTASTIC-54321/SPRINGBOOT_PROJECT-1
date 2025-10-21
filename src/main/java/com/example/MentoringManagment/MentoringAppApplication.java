package com.example.MentoringManagment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MentoringAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentoringAppApplication.class, args);
	}

}
