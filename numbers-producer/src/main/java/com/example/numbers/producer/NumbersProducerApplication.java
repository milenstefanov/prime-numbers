package com.example.numbers.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NumbersProducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(NumbersProducerApplication.class, args);
  }
}
