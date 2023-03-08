package com.hasksy.tba.app.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DateTimeServiceTest {

    @Mock
    private Clock clock;
    private AutoCloseable autoCloseable;
    private DateTimeService dateTimeService;

    private static final ZonedDateTime NOW = ZonedDateTime.of(
            2023, 1, 1, 12, 0, 59, 0, ZoneId.of("GMT")
    );

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        when(this.clock.getZone()).thenReturn(NOW.getZone());
        this.dateTimeService = new DateTimeService(clock);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void shouldGetPeriodMarker() {
        String periodMarker1 = dateTimeService.getPeriodMarker(0L);
        String periodMarker2 = dateTimeService.getPeriodMarker(1036328667000L);
        String periodMarker3 = dateTimeService.getPeriodMarker(1679781284000L);
        verify(clock, times(3)).getZone();
        assertEquals("Generated marker", "01.70.first", periodMarker1);
        assertEquals("Generated marker", "11.02.first", periodMarker2);
        assertEquals("Generated marker", "03.23.second", periodMarker3);
    }
}
