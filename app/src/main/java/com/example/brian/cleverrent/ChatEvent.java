package com.example.brian.cleverrent;

/**
 * Created by brian on 3/18/16.
 */
public class ChatEvent {
    String date;
    String indicator;
    String text;

    public ChatEvent() {}

    public ChatEvent(String date, String text) {
        this.date = date;
        this.text = text;
    }

    public String getDate() { return date; }

    public String getIndicator() { return indicator; }

    public String getText() { return text; }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatEvent) {
            ChatEvent temp = (ChatEvent) o;
            if (temp.getText().equals(this.getText())){
                return true;
            }
        }
        return false;
    }
}
