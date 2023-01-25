package com.hasksy.tba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class TbaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TbaApplication.class, args);
    }

    @PostConstruct
    private void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(
                System.getenv().getOrDefault("APPLICATION_TIMEZONE", "Europe/Moscow"))
        );
    }
}
