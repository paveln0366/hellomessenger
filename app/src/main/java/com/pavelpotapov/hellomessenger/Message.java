package com.pavelpotapov.hellomessenger;

public class Message {

    private String author;
    private String textOfMessage;

    public Message() {
    }

    public Message(String author, String textOfMessage) {
        this.author = author;
        this.textOfMessage = textOfMessage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTextOfMessage() {
        return textOfMessage;
    }

    public void setTextOfMessage(String textOfMessage) {
        this.textOfMessage = textOfMessage;
    }
}
