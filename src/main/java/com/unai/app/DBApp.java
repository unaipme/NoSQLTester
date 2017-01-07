package com.unai.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DBApp {
	
	public static void main(String [] args) {
		//BasicConfigurator.configure();
		SpringApplication.run(DBApp.class, args);
	}
	
}
