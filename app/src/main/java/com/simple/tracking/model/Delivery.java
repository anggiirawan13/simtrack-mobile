package com.simple.tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Delivery implements Serializable  {

    private int id;
    @SerializedName("delivery_number")
    private String deliveryNumber;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("shipper_id")
    private int shipperId;
    private String status;
    @SerializedName("delivery_date")
    private Timestamp deliveryDate;
    @SerializedName("receive_date")
    private Timestamp receiveDate;
    @SerializedName("confirmation_code")
    private String confirmationCode;
    private DeliveryRecipient recipient;
    private DeliveryHistoryLocation history;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public DeliveryRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(DeliveryRecipient recipient) {
        this.recipient = recipient;
    }

    public DeliveryHistoryLocation getHistory() {
        return history;
    }

    public void setHistory(DeliveryHistoryLocation history) {
        this.history = history;
    }
}
