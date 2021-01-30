package com.hard.playback_player.models;

import java.io.Serializable;

public abstract class AbstractModel implements Serializable {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
