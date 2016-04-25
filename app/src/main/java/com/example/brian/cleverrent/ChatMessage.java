package com.example.brian.cleverrent;

/**
 * Created by brian on 3/18/16.
 */
public class ChatMessage {
    String chatTime;
    String message;
    String from;

    public ChatMessage() {}

    public ChatMessage(String chatTime, String message, String from) {
        this.chatTime = chatTime;
        this.message = message;
        this.from = from;
    }

    public String getChatTime() { return chatTime; }

    public String getMessage() { return message; }

    public String getFrom() { return from; }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatMessage){
            ChatMessage temp = (ChatMessage) o;
            if (temp.getMessage().equals(this.getMessage()) && temp.getChatTime().equals(this.getChatTime())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}