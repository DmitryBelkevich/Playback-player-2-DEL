package com.hard.playback_player.settings;

public class Settings {
    private static int volume = 100;

    public static int getVolume() {
        return volume;
    }

    public static void setVolume(int volume) {
        Settings.volume = volume;
    }
}
