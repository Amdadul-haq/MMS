package com.example.mosque_management_system.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.models.DonationRequest;

import java.util.List;

public class DonationHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_DONATION = 0;
    private static final int ITEM_TYPE_FOOTER = 1;

    private List<DonationRequest> donationList;
    private boolean showFooter = false;

    public DonationHistoryAdapter(List<DonationRequest> donationList) {
        this.donationList = donationList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == donationList.size() && showFooter) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_DONATION;
    }

    @Override
    public int getItemCount() {
        return donationList.size() + (showFooter ? 1 : 0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_history, parent, false);
            return new DonationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DonationViewHolder) {
            DonationRequest donation = donationList.get(position);
            DonationViewHolder donationHolder = (DonationViewHolder) holder;

            donationHolder.donorNameTextView.setText(donation.getDonorName());
            donationHolder.donationTypeTextView.setText(donation.getDonationType());
            donationHolder.amountTextView.setText(donation.getAmount());
            donationHolder.donationMonthTextView.setText(donation.getDonationMonth());
            donationHolder.paymentMethodTextView.setText(donation.getPaymentMethod());
        }
        // Footer doesn't need binding logic
    }

    public void updateList(List<DonationRequest> filteredList) {
        this.donationList = filteredList;
        notifyDataSetChanged();
    }

    public void setShowFooter(boolean showFooter) {
        this.showFooter = showFooter;
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

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
