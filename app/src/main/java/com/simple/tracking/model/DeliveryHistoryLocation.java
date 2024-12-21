package com.simple.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryHistoryLocation implements Serializable {

    private int id;
    @SerializedName("delivery_id")
    private int deliveryId;
    private String latitude;
    private String longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getdeliveryId() {
        return deliveryId;
    }

    public void setdeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
