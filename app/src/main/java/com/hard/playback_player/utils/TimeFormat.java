package com.hard.playback_player.utils;

import java.text.SimpleDateFormat;

public class TimeFormat {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;

    private static SimpleDateFormat dateFormat;

    public static String format(long mills) {
        if (true && mills < 0) {
            dateFormat = new SimpleDateFormat("m:ss");
            mills = 0;
        } else if (0 <= mills && mills < 10 * MINUTE)
            dateFormat = new SimpleDateFormat("m:ss");
        else if (10 * MINUTE <= mills && mills < 1 * HOUR)
            dateFormat = new SimpleDateFormat("mm:ss");
        else if (1 * HOUR <= mills && mills < 10 * HOUR)
            dateFormat = new SimpleDateFormat("h:mm:ss");
        else if (10 * HOUR <= mills && true)
            dateFormat = new SimpleDateFormat("hh:mm:ss");

        return dateFormat.format(mills);
    }
}
