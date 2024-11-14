package com.simple.tracking.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    private int id;

    private String whatsapp;
    private String street;
    @SerializedName("sub_district")
    private String subDistrict;
    private String district;
    private String city;
    private String province;
    @SerializedName("postal_code")
    private String postalCode;

    public Address() {
    }

    private Address(Builder builder) {
        this.id = builder.id;
        this.whatsapp = builder.whatsapp;
        this.street = builder.street;
        this.subDistrict = builder.subDistrict;
        this.district = builder.district;
        this.city = builder.city;
        this.province = builder.province;
        this.postalCode = builder.postalCode;
    }

    public int getId() {
        return id;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getStreet() {
        return street;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public static class Builder {
        private int id;
        private String whatsapp;
        private String street;
        private String subDistrict;
        private String district;
        private String city;
        private String province;
        private String postalCode;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setWhatsapp(String whatsapp) {
            this.whatsapp = whatsapp;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setSubDistrict(String subDistrict) {
            this.subDistrict = subDistrict;
            return this;
        }

        public Builder setDistrict(String district) {
            this.district = district;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setProvince(String province) {
            this.province = province;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return street + ", " + subDistrict + ", " + district + ", " + city + ", " + province + ", " + postalCode;
    }
}
