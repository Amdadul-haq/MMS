package com.example.mosque_management_system.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.models.DonationRequest;

import java.util.List;

public class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.DonationViewHolder> {

    private List<DonationRequest> donationList;

    public DonationHistoryAdapter(List<DonationRequest> donationList) {
        this.donationList = donationList;
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_history, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationViewHolder holder, int position) {
        DonationRequest donation = donationList.get(position);

        holder.donorNameTextView.setText(donation.getDonorName());
        holder.donationTypeTextView.setText(donation.getDonationType());
        holder.amountTextView.setText(donation.getAmount());
        holder.donationMonthTextView.setText(donation.getDonationMonth());
        holder.paymentMethodTextView.setText(donation.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public void updateList(List<DonationRequest> filteredList) {
        this.donationList = filteredList;
        notifyDataSetChanged();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        TextView donorNameTextView, donationTypeTextView, amountTextView, donationMonthTextView, paymentMethodTextView;

        public DonationViewHolder(View itemView) {
            super(itemView);
            donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
            donationTypeTextView = itemView.findViewById(R.id.donationTypeTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            donationMonthTextView = itemView.findViewById(R.id.donationMonthTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
        }
    }
}
