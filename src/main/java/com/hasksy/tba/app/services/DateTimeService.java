package com.hasksy.tba.app.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Business logic for working with time of application and received timestamps
 */
@RequiredArgsConstructor
public class DateTimeService {

    private static final Logger log = LoggerFactory.getLogger(DateTimeService.class);

    private final Clock clock;

    /**
     * Creates simple so-called "Period marker" which identifies in which
     * spreadsheet message parsing result should be put
     * Period marker has the following structure:
     *      'MM.yy.<first | second>'
     *      "MM" - number of month (Example: 07)
     *      "yy" - year (Example: 23)
     *      "<first | second>" - String which identifies, whether message
     *      was passed before and on 15th of current month or after
     * @param timestampMillis timestamp of received message
     * @return Period marker string
     */
    public String getPeriodMarker(Long timestampMillis) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), clock.getZone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.yy");
        String formatted = time.format(formatter);
        if (time.getDayOfMonth() <= 15) {
            return formatted+".first";
        }
        return formatted+".second";
    }

}
