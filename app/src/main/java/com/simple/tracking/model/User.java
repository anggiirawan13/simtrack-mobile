package com.simple.tracking.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private final int id;
    private final String password;
    private final String fullname;
    private final String username;
    private final String role;
    private final Address address;

    private User(Builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.fullname = builder.fullname;
        this.username = builder.username;
        this.role = builder.role;
        this.address = builder.address;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }

    @NonNull
    @Override
    public String toString() {
        return fullname;
    }

    public static class Builder {
        private int id;
        private String password;
        private String fullname;
        private String username;
        private String role;
        private Address address;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setFullname(String fullname) {
            this.fullname = fullname;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
