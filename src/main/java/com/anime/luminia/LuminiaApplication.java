package com.anime.luminia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LuminiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuminiaApplication.class, args);
    }

}
