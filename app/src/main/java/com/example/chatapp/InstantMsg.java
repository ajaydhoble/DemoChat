package com.example.chatapp;

public class InstantMsg {

    private String message;
    private String author;

    public InstantMsg(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public InstantMsg() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
