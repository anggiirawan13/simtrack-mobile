package com.simple.tracking.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private final int id;
    private final String password;
    private final String fullname;
    private final String username;
    @SerializedName("role_id")
    private final int roleId;
    private final Role role;
    @SerializedName("address_id")
    private final int addressId;
    private final Address address;
    private final Shipper shipper;

    private User(Builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.fullname = builder.fullname;
        this.username = builder.username;
        this.role = builder.role;
        this.address = builder.address;
        this.shipper = builder.shipper;
        this.roleId = builder.roleId;
        this.addressId = builder.addressId;
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

    public int getRoleId() {
        return roleId;
    }

    public Role getRole() {
        return role;
    }

    public int getAddressId() {
        return addressId;
    }

    public Address getAddress() {
        return address;
    }

    public Shipper getShipper() {
        return shipper;
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
        private int roleId;
        private Role role;
        private int addressId;
        private Address address;
        private Shipper shipper;

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


        public Builder setRoleId(int role_id) {
            this.roleId = role_id;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setAddressId(int address_id) {
            this.addressId = address_id;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public void setShipper(Shipper shipper) {
            this.shipper = shipper;
        }

        public User build() {
            return new User(this);
        }
    }
}
