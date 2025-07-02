package com.example.mosque_management_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mosque_management_system.api.MosqueAPI;
import com.example.mosque_management_system.models.CreateMosqueResponse;
import com.example.mosque_management_system.models.Mosque;
import com.example.mosque_management_system.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateMosqueActivity extends AppCompatActivity {

    EditText mosqueNameInput, mosqueAddressInput, villageInput, unionInput, upazilaInput, zillaInput, imamNameInput;
    Button btnCreateMosqueSubmit;

    MosqueAPI mosqueAPI;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_mosque);

        mosqueNameInput = findViewById(R.id.mosqueNameInput);
        mosqueAddressInput = findViewById(R.id.mosqueAddressInput);
        villageInput = findViewById(R.id.villageInput);
        unionInput = findViewById(R.id.unionInput);
        upazilaInput = findViewById(R.id.upazilaInput);
        zillaInput = findViewById(R.id.zillaInput);
        imamNameInput = findViewById(R.id.imamNameInput);
        btnCreateMosqueSubmit = findViewById(R.id.btnCreateMosqueSubmit);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        token = prefs.getString("jwt_token", null);

        if (token == null) {
            Toast.makeText(this, "You are not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(token);
        mosqueAPI = retrofit.create(MosqueAPI.class);

        btnCreateMosqueSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                createMosque();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(mosqueNameInput.getText().toString().trim())) {
            mosqueNameInput.setError("Mosque Name is required");
            mosqueNameInput.requestFocus();
            return false;
        }
        return true;
    }

    private void createMosque() {
        Mosque mosque = new Mosque(
                mosqueNameInput.getText().toString().trim(),
                mosqueAddressInput.getText().toString().trim(),
                villageInput.getText().toString().trim(),
                unionInput.getText().toString().trim(),
                upazilaInput.getText().toString().trim(),
                zillaInput.getText().toString().trim(),
                imamNameInput.getText().toString().trim()
        );

        Call<CreateMosqueResponse> call = mosqueAPI.createMosque(mosque);
        call.enqueue(new Callback<CreateMosqueResponse>() {
            @Override
            public void onResponse(Call<CreateMosqueResponse> call, Response<CreateMosqueResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Success block

                        Toast.makeText(CreateMosqueActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CreateMosqueActivity.this, DashboardActivity.class);
                        intent.putExtra("mosqueId", response.body().getMosqueId());
                        intent.putExtra("mosqueCode", response.body().getMosqueCode());
                        startActivity(intent);
                        finish();

                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("MOSQUE_CREATE_ERROR", errorBody);
                        Toast.makeText(CreateMosqueActivity.this, "Error: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(Call<CreateMosqueResponse> call, Throwable t) {
                Toast.makeText(CreateMosqueActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
