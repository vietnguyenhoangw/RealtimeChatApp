package com.example.realtimechat;

public class Message {
    String id;
    String user_name;
    String message;

    public Message(String id,String user, String message) {
        this.user_name = user;
        this.message = message;
        this.id = id;
    }

    public Message(String user, String message) {
        this.user_name = user;
        this.message = message;
    }

    public Message() {
    }

    public String getUser() {
        return user_name;
    }

    public void setUser(String user) {
        this.user_name = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
