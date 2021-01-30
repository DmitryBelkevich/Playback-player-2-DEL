package com.hard.playback_player.models;

import java.util.Map;

public class Song extends AbstractNamedModel {
    private Band band;
    private String text;
    private String keySignature;
    private Map<String, String> scores;
    private String original;
    private Map<Byte, String> playbacks;

    // states
    private String score;
    private boolean originalPlaying;
    private byte transposition;

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKeySignature() {
        return keySignature;
    }

    public void setKeySignature(String keySignature) {
        this.keySignature = keySignature;
    }

    public Map<String, String> getScores() {
        return scores;
    }

    public void setScores(Map<String, String> scores) {
        this.scores = scores;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Map<Byte, String> getPlaybacks() {
        return playbacks;
    }

    public void setPlaybacks(Map<Byte, String> playbacks) {
        this.playbacks = playbacks;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isOriginalPlaying() {
        return originalPlaying;
    }

    public void setOriginalPlaying(boolean originalPlaying) {
        this.originalPlaying = originalPlaying;
    }

    public byte getTransposition() {
        return transposition;
    }

    public void setTransposition(byte transposition) {
        this.transposition = transposition;
    }

    public String getCurrentScore() {
        return scores.get(score);
    }

    public String getCurrentPlayback() {
        if (originalPlaying)
            return original;

        return playbacks.get(transposition);
    }
}
