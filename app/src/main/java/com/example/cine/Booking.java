package com.example.cine;

import java.io.Serializable;

public class Booking implements Serializable {
    private String movieTitle;
    private String date;
    private String time;
    private int seats;
    private double totalAmount;
    private long timestamp;

    public Booking() {}

    public Booking(String movieTitle, String date, String time, int seats, double totalAmount, long timestamp) {
        this.movieTitle = movieTitle;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
    }

    public String getMovieTitle() { return movieTitle; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getSeats() { return seats; }
    public double getTotalAmount() { return totalAmount; }
    public long getTimestamp() { return timestamp; }
}
