package com.example.cine;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String genre;
    private String duration;
    private int imageResId;
    private boolean isComingSoon;

    public Movie(String title, String genre, String duration, int imageResId, boolean isComingSoon) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.imageResId = imageResId;
        this.isComingSoon = isComingSoon;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getImageResId() { return imageResId; }
    public boolean isComingSoon() { return isComingSoon; }
}