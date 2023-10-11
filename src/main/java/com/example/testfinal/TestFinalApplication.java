package com.example.testfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TestFinalApplication {

	public static void main(String[] args) {
//		TestCsvGenerator.generateEventsFile();
		SpringApplication.run(TestFinalApplication.class, args);
	}

}
