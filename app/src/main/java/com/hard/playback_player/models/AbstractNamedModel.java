package com.hard.playback_player.models;

public abstract class AbstractNamedModel extends AbstractModel {
    protected String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
