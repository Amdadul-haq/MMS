package com.example.mosque_management_system.admin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.R;
import com.example.mosque_management_system.adapters.JoinRequestAdapter;
import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.JoinRequest;
import com.example.mosque_management_system.models.JoinRequestListResponse;
import com.example.mosque_management_system.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinRequestListFragment extends Fragment {

    private RecyclerView recyclerView;
    private JoinRequestAdapter adapter;
    private String token;

    public JoinRequestListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_requests, container, false);

        recyclerView = view.findViewById(R.id.joinRequestRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        token = prefs.getString("jwt_token", null);

        loadJoinRequests();

        return view;
    }

    private void loadJoinRequests() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
        MosqueAPI api = retrofit.create(MosqueAPI.class);

        api.getPendingJoinRequests().enqueue(new Callback<JoinRequestListResponse>() {
            @Override
            public void onResponse(Call<JoinRequestListResponse> call, Response<JoinRequestListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<JoinRequest> requestList = response.body().getRequests();
                    adapter = new JoinRequestAdapter(requireContext(), requestList, token);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load requests", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JoinRequestListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
