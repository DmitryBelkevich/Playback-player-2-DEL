package com.hard.playback_player;

import com.hard.playback_player.utils.TimeFormat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeFormatTest {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;

    private String time;

    @Test
    @DisplayName("should return m:ss when time is negative")
    public void shouldReturnZero_when_Time_IsNegative() {
        time = TimeFormat.format(-1);
        assertEquals("0:00", time);
    }

    @Test
    @DisplayName("should return m:ss when (time >= 0) and (time < 10 minutes)")
    public void shouldReturnTime_when_Time_EqualsAndMoreZero_AndLess_TenMinutes() {
        time = TimeFormat.format(0);
        assertEquals("0:00", time);

        time = TimeFormat.format(999);
        assertEquals("0:00", time);

        time = TimeFormat.format(1 * SECOND);
        assertEquals("0:01", time);

        time = TimeFormat.format(9 * MINUTE + 59 * SECOND);
        assertEquals("9:59", time);
    }

    @Test
    @DisplayName("should return mm:ss when (time >= 10 minutes) and (time < 1 hour)")
    public void shouldReturnTime_when_Time_EqualsAndMore_tenMinutes_and_Less_OneHour() {
        time = TimeFormat.format(10 * MINUTE);
        assertEquals("10:00", time);

        time = TimeFormat.format(59 * MINUTE + 59 * SECOND);
        assertEquals("59:59", time);
    }

    @Test
    @DisplayName("should return h:mm:ss when (time >= 1 hour) and (time < 10 hours)")
    public void shouldReturnTime_when_Time_EqualsAndMore_OneHour_and_Less_TenHours() {
        time = TimeFormat.format(1 * HOUR);
        assertEquals("1:00:00", time);

        time = TimeFormat.format(9 * HOUR + 59 * MINUTE + 59 * SECOND);
        assertEquals("9:59:59", time);
    }

    @Test
    @DisplayName("should return hh:mm:ss when (time >= 10 hour)")
    public void shouldReturnTime_when_Time_EqualsAndMore_TenHours() {
        time = TimeFormat.format(10 * HOUR);
        assertEquals("10:00:00", time);
    }
}
