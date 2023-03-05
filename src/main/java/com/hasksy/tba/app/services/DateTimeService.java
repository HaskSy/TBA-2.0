package com.hasksy.tba.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Calendar;
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
     * @param date date or received message
     * @param format date and time pattern string, see <a href="https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a> JavaDoc
     * @return formatted date
     */
    public static String getTimeInFormat(Date date, String format) {
        DateFormat dtFormat = new SimpleDateFormat(format);
        return dtFormat.format(date);
    }

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
    public static String getPeriodMarker(Long timestampMillis) {
        Calendar cdr = Calendar.getInstance();
        cdr.setTimeInMillis(timestampMillis);
        String formatted = getTimeInFormat(cdr.getTime(), "MM.yy");
        if (cdr.get(Calendar.DAY_OF_MONTH) <= 15) {
            return formatted+".first";
        }
        return formatted+".second";
    }

}
