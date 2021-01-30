package com.hard.playback_player.services;

import com.hard.playback_player.models.Song;
import com.hard.playback_player.repositories.SongRepository;
import com.hard.playback_player.settings.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SongService {
    private SongRepository songRepository;

    public SongService() {
        this.songRepository = new SongRepository();
    }

    public Collection<Song> getAll() {
        Collection<Song> songs = songRepository.getAll();

        // set states
        Iterator<Song> iterator = songs.iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();

            // score
            Map<String, String> scores = song.getScores();
            if (!scores.isEmpty()) {
                List<String> scoresTitles = new ArrayList<>(scores.keySet());

                String score;

                String defaultScore = Constants.FULL_SCORE;

                if (scoresTitles.contains(defaultScore))
                    score = defaultScore;
                else
                    score = scoresTitles.get(0);

                song.setScore(score);
            }

            // originalPlaying
            if (song.getPlaybacks() == null || song.getPlaybacks().isEmpty())
                song.setOriginalPlaying(true);
            else
                song.setOriginalPlaying(false);

            // transposition
            Map<Byte, String> playbacks = song.getPlaybacks();
            Set<Byte> transpositions = playbacks.keySet();

            if (!transpositions.isEmpty()) {
                byte transposition;

                byte defaultTransposition = 0;

                if (transpositions.contains(defaultTransposition))
                    transposition = defaultTransposition;
                else
                    transposition = Collections.min(transpositions);

                song.setTransposition(transposition);
            }
        }

        return songs;
    }
}
