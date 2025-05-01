// PaginatedDonationResponse.java
package com.example.mosque_management_system.models;

import java.util.List;

public class PaginatedDonationResponse {
    private List<DonationRequest> donations;
    private int currentPage;
    private int totalPages;
    private int totalDonations;

    public List<DonationRequest> getDonations() {
        return donations;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalDonations() {
        return totalDonations;
    }
}
