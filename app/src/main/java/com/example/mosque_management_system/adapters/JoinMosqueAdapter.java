package com.example.mosque_management_system.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.JoinRequestBody;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinMosqueAdapter extends RecyclerView.Adapter<JoinMosqueAdapter.MosqueViewHolder> {

    private Context context;
    private List<Mosque> mosqueList;
    private String token;

    public JoinMosqueAdapter(Context context, List<Mosque> mosqueList, String token) {
        this.context = context;
        this.mosqueList = mosqueList;
        this.token = token;
    }

    @NonNull
    @Override
    public MosqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mosque_list_item, parent, false);
        return new MosqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueViewHolder holder, int position) {
        Mosque mosque = mosqueList.get(position);

        holder.txtMosqueName.setText(mosque.getName());
        holder.txtMosqueLocation.setText("Union: " + mosque.getUnionName() + ", Upazila: " + mosque.getUpazila());

        holder.btnJoinMosque.setOnClickListener(v -> {
            Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
            MosqueAPI api = retrofit.create(MosqueAPI.class);

            JoinRequestBody requestBody = new JoinRequestBody(mosque.getId());

            Call<Void> call = api.sendJoinRequest(requestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Join request sent!", Toast.LENGTH_SHORT).show();
                        holder.btnJoinMosque.setText("Requested");
                        holder.btnJoinMosque.setEnabled(false);
                    } else {
                        Toast.makeText(context, "Already requested or server error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mosqueList.size();
    }

    public static class MosqueViewHolder extends RecyclerView.ViewHolder {
        TextView txtMosqueName, txtMosqueLocation;
        Button btnJoinMosque;

        public MosqueViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMosqueName = itemView.findViewById(R.id.txtMosqueName);
            txtMosqueLocation = itemView.findViewById(R.id.txtMosqueLocation);
            btnJoinMosque = itemView.findViewById(R.id.btnJoinMosque);
        }
    }
}
