package com.hasksy.tba;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class TbaApplication {

    @Generated
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
