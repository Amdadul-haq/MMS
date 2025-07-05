package com.example.mosque_management_system.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.GenericResponse;
import com.example.mosque_management_system.models.JoinRequest;
import com.example.mosque_management_system.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinRequestAdapter extends RecyclerView.Adapter<JoinRequestAdapter.ViewHolder> {

    private Context context;
    private List<JoinRequest> requestList;
    private String token;

    public JoinRequestAdapter(Context context, List<JoinRequest> requestList, String token) {
        this.context = context;
        this.requestList = requestList;
        this.token = token;
    }

    @NonNull
    @Override
    public JoinRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_join_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinRequestAdapter.ViewHolder holder, int position) {
        JoinRequest request = requestList.get(position);

        holder.textUserName.setText(request.getUserId().getFullName());
        holder.textMosqueName.setText(request.getMosqueId().getName());

        holder.btnApprove.setOnClickListener(v -> handleAction(request.getId(), true, position));
        holder.btnReject.setOnClickListener(v -> handleAction(request.getId(), false, position));
    }

    private void handleAction(String requestId, boolean isApprove, int position) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
        MosqueAPI mosqueAPI = retrofit.create(MosqueAPI.class);

        Call<GenericResponse> call = isApprove ?
                mosqueAPI.approveJoinRequest(requestId) :
                mosqueAPI.rejectJoinRequest(requestId);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    requestList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Failed to update request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textMosqueName;
        ImageView btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);
            textMosqueName = itemView.findViewById(R.id.textMosqueName);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
