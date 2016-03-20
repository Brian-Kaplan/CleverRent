package com.example.brian.cleverrent;

/**
 * Created by brian on 3/18/16.
 */
public class ChatMessage {
    String time;
    String message;
    String from;

    public ChatMessage() {}

    public ChatMessage(String time, String message, String from) {
        this.time = time;
        this.message = message;
        this.from = from;
    }

    public String getTime() { return time; }

    public String getMessage() { return message; }

    public String getFrom() { return from; }

}