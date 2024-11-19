package com.simple.tracking.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shipper implements Serializable {

    private final int id;
    @SerializedName("user_id")
    private final int userId;
    @SerializedName("device_mapping")
    private final String deviceMapping;

    private final User user;

    // Constructor private agar hanya bisa dibuat melalui builder
    private Shipper(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.deviceMapping = builder.deviceMapping;
        this.user = builder.user;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDeviceMapping() {
        return deviceMapping;
    }

    public User getUser() {
        return user;
    }

    public static class Builder {
        private int id;
        private int userId;
        private String deviceMapping;
        private User user;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setDeviceMapping(String deviceMapping) {
            this.deviceMapping = deviceMapping;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Shipper build() {
            return new Shipper(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return user != null ? user.getFullname() : "Unknown User";
    }
}
