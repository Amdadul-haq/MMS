package com.example.mosque_management_system.repository;

import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MosqueRepository {
    private MosqueAPI mosqueApi;

    public MosqueRepository(String token) {
        mosqueApi = RetrofitClient.getRetrofitInstance(token).create(MosqueAPI.class);
    }

    public void getMosqueById(String mosqueId, final MosuqeCallback callback) {
        Call<Mosque> call = mosqueApi.getMosqueById(mosqueId);
        call.enqueue(new Callback<Mosque>() {
            @Override
            public void onResponse(Call<Mosque> call, Response<Mosque> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Mosque> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface MosuqeCallback {
        void onSuccess(Mosque mosque);
        void onError(String errorMessage);
    }
}
