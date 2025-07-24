package com.aircargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AirCargoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirCargoApplication.class, args);
    }
} 