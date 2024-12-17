package com.simple.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status implements Serializable {

    private final int id;
    @SerializedName("status")
    private final String status;

    // Constructor private agar hanya bisa dibuat melalui builder
    private Status(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public static class Builder {
        private int id;
        private String status;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDeviceMapping(String status) {
            this.status = status;
            return this;
        }

        public Status build() {
            return new Status(this);
        }
    }
}
