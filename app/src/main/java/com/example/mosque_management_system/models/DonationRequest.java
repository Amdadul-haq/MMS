package com.example.mosque_management_system.models;

public class DonationRequest {
    private String donorName;
    private String donationType;
    private String donationMonth;
    private String amount;
    private String paymentMethod;

    public DonationRequest(String donorName, String donationType, String donationMonth, String amount, String paymentMethod) {
        this.donorName = donorName;
        this.donationType = donationType;
        this.donationMonth = donationMonth;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}
