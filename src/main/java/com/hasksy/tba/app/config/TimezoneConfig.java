package com.hasksy.tba.app.config;

import com.hasksy.tba.app.services.DateTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class TimezoneConfig {

    /**
     * @return A clock providing access to the current instant, date and time using a timezone.
     */
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(TimeZone.getDefault().getID()));
    }

    /**
     * @param clock Application clock
     * @return {@link DateTimeService}
     */
    @Bean
    public DateTimeService dateTimeService(Clock clock) {
        return new DateTimeService(clock);
    }
}
