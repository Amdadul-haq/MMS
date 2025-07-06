package com.example.mosque_management_system.models;

public class DonationRequest {
    private String donorName;
    private String donationType;
    private String donationMonth;
    private double amount;
    private String paymentMethod;
    private String mosqueId; // ✅ NEW FIELD

    public DonationRequest(String donorName, String donationType, String donationMonth, double amount, String paymentMethod, String mosqueId) {
        this.donorName = donorName;
        this.donationType = donationType;
        this.donationMonth = donationMonth;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.mosqueId = mosqueId;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonationType() {
        return donationType;
    }

    public String getDonationMonth() {
        return donationMonth;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getMosqueId() {
        return mosqueId;
    }
}
