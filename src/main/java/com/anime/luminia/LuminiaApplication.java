package com.anime.luminia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class LuminiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuminiaApplication.class, args);
    }

}
