package com.example.mosque_management_system;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mosque_management_system.adapters.JoinMosqueAdapter;
import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.models.MosqueListResponse;
import com.example.mosque_management_system.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinMosqueActivity extends AppCompatActivity {

    private RecyclerView mosqueRecyclerView;
    private JoinMosqueAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_mosque);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        token = prefs.getString("jwt_token", null);

        mosqueRecyclerView = findViewById(R.id.mosqueRecyclerView);
        mosqueRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMosques();
    }

    private void loadMosques() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
        MosqueAPI mosqueAPI = retrofit.create(MosqueAPI.class);

        Call<MosqueListResponse> call = mosqueAPI.getAllMosques();
        call.enqueue(new Callback<MosqueListResponse>() {
            @Override
            public void onResponse(Call<MosqueListResponse> call, Response<MosqueListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Mosque> mosqueList = response.body().getMosques();
                    adapter = new JoinMosqueAdapter(JoinMosqueActivity.this, mosqueList, token);
                    mosqueRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(JoinMosqueActivity.this, "Failed to load mosques", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MosqueListResponse> call, Throwable t) {
                Toast.makeText(JoinMosqueActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
