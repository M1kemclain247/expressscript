package com.m1kes.expressscript.objects;

import com.m1kes.expressscript.objects.custom.CustomDate;

import java.util.List;

public class Order {

    private int clientID;
    private String deviceRef;
    private String description;
    private CustomDate transaction_date;
    private String address;
    private double total;
    private PaymentMode paymentMode;
    private List<QuoteItem> drugs;

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getDeviceRef() {
        return deviceRef;
    }

    public void setDeviceRef(String deviceRef) {
        this.deviceRef = deviceRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomDate getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(CustomDate transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<QuoteItem> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<QuoteItem> drugs) {
        this.drugs = drugs;
    }

    @Override
    public String toString() {
        return "Order{" +
                "clientID=" + clientID +
                ", deviceRef='" + deviceRef + '\'' +
                ", description='" + description + '\'' +
                ", transaction_date=" + transaction_date +
                ", address='" + address + '\'' +
                ", total=" + total +
                ", paymentMode=" + paymentMode +
                ", drugs=" + drugs +
                '}';
    }
}
