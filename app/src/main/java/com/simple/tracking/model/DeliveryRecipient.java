package com.simple.tracking.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryRecipient extends Address implements Serializable {

    private int id;
    @SerializedName("delivery_number")
    private String deliveryNumber;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}