package com.example.test_tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TestTasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestTasksApplication.class, args);
	}

}
