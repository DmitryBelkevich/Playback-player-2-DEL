package com.hard.playback_player.repositories;

import com.hard.playback_player.models.Band;
import com.hard.playback_player.models.Song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SongRepository {
    private BandRepository bandRepository;

    public SongRepository() {
        this.bandRepository = new BandRepository();
    }

    public Collection<Song> getAll() {
        Collection<Song> songs = new ArrayList<>();

        Collection<Band> bands = bandRepository.getAll();

        Iterator<Band> bandIterator = bands.iterator();
        while (bandIterator.hasNext()) {
            Band band = bandIterator.next();
            Collection<Song> songsInCurrentBand = band.getSongs();
            Iterator<Song> songIterator = songsInCurrentBand.iterator();
            while (songIterator.hasNext()) {
                Song song = songIterator.next();
                songs.add(song);
            }
        }

        return songs;
    }
}
