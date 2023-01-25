package com.hasksy.tba.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;

/**
 * Business logic for working with time of application and received timestamps
 */
public class DateTimeService {

    private static final Logger log = LoggerFactory.getLogger(DateTimeService.class);

    private final Clock clock;

    public DateTimeService(Clock clock) {
        this.clock = clock;
    }

    /**
     * @param timestampMillis timestamp in milliseconds
     * @param format date and time pattern string, see <a href="https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a> JavaDoc
     * @return formatted date
     */
    public static String getTimeInFormat(long timestampMillis, String format) {
        DateFormat dtFormat = new SimpleDateFormat(format);
        return dtFormat.format(new Date(timestampMillis));
    }

}
