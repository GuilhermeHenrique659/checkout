package com.example.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckoutApplication {

    static void main(String[] args) {

        SpringApplication.run(CheckoutApplication.class, args);
    }

}
