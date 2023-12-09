package com.fastcampus.mini9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Mini9Application {

    public static void main(String[] args) {
        SpringApplication.run(Mini9Application.class, args);
    }

}
