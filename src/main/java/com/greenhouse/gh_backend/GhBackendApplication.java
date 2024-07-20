package com.greenhouse.gh_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GhBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhBackendApplication.class, args);
    }

}
