package com.justice_e_foster.music_application.model;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String path;

    public Song(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}