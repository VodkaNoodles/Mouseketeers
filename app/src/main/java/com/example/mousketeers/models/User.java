package com.example.mousketeers.models;

public class User {
    private String username;
    private int score;

    public User() {
        // Required empty constructor for Firestore
    }

    public User(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
