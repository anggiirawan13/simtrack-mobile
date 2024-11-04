package com.simple.tracking.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shipper implements Serializable {

    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("device_mapping")
    private String deviceMapping;

    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceMapping() {
        return deviceMapping;
    }

    public void setDeviceMapping(String deviceMapping) {
        this.deviceMapping = deviceMapping;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return user.getFullname();
    }

}
