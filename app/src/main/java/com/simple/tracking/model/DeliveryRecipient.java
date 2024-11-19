package com.simple.tracking.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryRecipient implements Serializable {

    private int id;
    @SerializedName("delivery_number")
    private String deliveryNumber;
    private String name;
    private Address address;

    public DeliveryRecipient() {
    }

    private DeliveryRecipient(Builder builder) {
        this.id = builder.id;
        this.deliveryNumber = builder.deliveryNumber;
        this.name = builder.name;
        this.address = builder.address;
    }

    public int getId() {
        return id;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeliveryRecipient{" + "id=" + id + ", deliveryNumber='" + deliveryNumber + '\'' + ", name='" + name + '\'' + '}';
    }

    public static class Builder {
        private int id;
        private String deliveryNumber;
        private String name;
        private Address address;


        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDeliveryNumber(String deliveryNumber) {
            this.deliveryNumber = deliveryNumber;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public DeliveryRecipient build() {
            return new DeliveryRecipient(this);
        }
    }
}
