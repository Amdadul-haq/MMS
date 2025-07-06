package com.example.mosque_management_system.models;

public class DonationSummaryResponse {
    private boolean success;
    private double totalAmount;

    public boolean isSuccess() { return success; }
    public double getTotalDonation() { return totalAmount; }
}
