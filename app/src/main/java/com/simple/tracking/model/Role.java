package com.simple.tracking.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role implements Serializable {

    private final int id;
    @SerializedName("role")
    private final String role;

    // Constructor private agar hanya bisa dibuat melalui builder
    private Role(Builder builder) {
        this.id = builder.id;
        this.role = builder.role;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public static class Builder {
        private int id;
        private String role;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDeviceMapping(String role) {
            this.role = role;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }
}
