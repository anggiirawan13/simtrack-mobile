package com.simple.tracking.network;

public class BaseResponse<T> {
    private boolean success;
    private String messages;
    private T data; // This can be a List<User> or another object

    // Constructor
    public BaseResponse(boolean success, String messages, T data) {
        this.success = success;
        this.messages = messages;
        this.data = data;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessages() {
        return messages;
    }

    public T getData() {
        return data;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void setData(T data) {
        this.data = data;
    }
}
