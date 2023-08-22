package com.borymskyi.exchangeratesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ExchangeRatesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatesAppApplication.class, args);
    }

}
