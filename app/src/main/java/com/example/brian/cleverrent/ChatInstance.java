package com.example.brian.cleverrent;

import java.util.ArrayList;

/**
 * Created by brian on 3/16/16.
 */
public class ChatInstance {

    ArrayList<ChatEvent> chatEventTimeline;
    ArrayList<ChatMessage> chatMessageTimeline;
    String identifier;
    String chatOwner;
    String chatParticipant;

    public ChatInstance() {}

    public ChatInstance(String chatOwner, String chatParticipant, String identifier) {
        this.chatOwner = chatOwner;
        this.chatParticipant = chatParticipant;
        this.identifier = identifier;
        this.chatEventTimeline = new ArrayList<>();
        this.chatMessageTimeline = new ArrayList<>();
    }

    public ArrayList<ChatEvent> getChatEventTimeline() { return chatEventTimeline; }

    public ArrayList<ChatMessage> getChatMessageTimeline() { return chatMessageTimeline; }

    public void addChatMessage(ChatMessage chatMessage) {chatMessageTimeline.add(chatMessage); }

    public void addChatEvent(ChatEvent chatEvent){
        chatEventTimeline.add(chatEvent);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getChatOwner() {
        return chatOwner;
    }

    public String getChatParticipant() { return chatParticipant; }

    public void setChatEventTimeline(ArrayList<ChatEvent> chatEventTimeline) {
        this.chatEventTimeline = chatEventTimeline;
    }

    public void setChatMessageTimeLine(ArrayList<ChatMessage> chatMessageTimeLine) {
        this.chatMessageTimeline = chatMessageTimeLine;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
