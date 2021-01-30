package com.hard.playback_player.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hard.playback_player.models.Band;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BandRepository {
    public Collection<Band> getAll() {
        Collection<Band> bands = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        String url = Constants.GOOGLE_DRIVE_FILE + Constants.DATABASE;

        try {
            long bandId = 1;
            long songId = 1;

            Collection<LinkedHashMap> bandsMap = new ArrayList<>();
            bandsMap = mapper.readValue(new URL(url), bandsMap.getClass());
            Iterator<LinkedHashMap> bandIterator = bandsMap.iterator();
            while (bandIterator.hasNext()) {
                LinkedHashMap bandMap = bandIterator.next();

                Band band = new Band();

                // band.id
                band.setId(bandId++);

                // band.title
                band.setTitle((String) bandMap.get("title"));

                // band.songs
                ArrayList<Song> songs = new ArrayList<>();

                ArrayList<LinkedHashMap> songsStrings = (ArrayList<LinkedHashMap>) bandMap.get("songs");
                Iterator<LinkedHashMap> songIterator = songsStrings.iterator();
                while (songIterator.hasNext()) {
                    LinkedHashMap songMap = songIterator.next();

                    Song song = new Song();

                    // song.id
                    song.setId(songId++);

                    // song.title
                    song.setTitle((String) songMap.get("title"));

                    // song.band
                    song.setBand(band);

                    // song.text
                    song.setText((String) songMap.get("text"));

                    // song.keySignature
                    song.setKeySignature((String) songMap.get("key_signature"));

                    // song.scores
                    Map<String, String> scores = new LinkedHashMap<>();
                    LinkedHashMap<String, String> scoresMap = (LinkedHashMap) songMap.get("scores");

                    if (scoresMap != null)
                        scores.putAll(scoresMap);

                    song.setScores(scores);

                    // song.original
                    song.setOriginal((String) songMap.get("original"));

                    // song.playbacks
                    Map<Byte, String> playbacks = new LinkedHashMap<>();

                    LinkedHashMap<String, String> playbacksMap = (LinkedHashMap<String, String>) songMap.get("playbacks");
                    if (playbacksMap != null) {
                        Iterator<Map.Entry<String, String>> playbacksIterator = playbacksMap.entrySet().iterator();
                        while (playbacksIterator.hasNext()) {
                            Map.Entry<String, String> entry = playbacksIterator.next();

                            byte key = Byte.valueOf(entry.getKey());
                            String value = entry.getValue();

                            playbacks.put(key, value);
                        }
                    }

                    song.setPlaybacks(playbacks);

                    // add songs
                    songs.add(song);
                }

                band.setSongs(songs);

                // add bands
                bands.add(band);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bands;
    }
}
