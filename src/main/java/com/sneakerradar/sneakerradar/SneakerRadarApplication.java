package com.sneakerradar.sneakerradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class SneakerRadarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SneakerRadarApplication.class, args);
    }

}
