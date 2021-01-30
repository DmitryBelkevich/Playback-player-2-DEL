package com.hard.playback_player.models;

import java.util.Collection;

public class Band extends AbstractNamedModel {
    private Collection<Song> songs;

    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
    }
}
