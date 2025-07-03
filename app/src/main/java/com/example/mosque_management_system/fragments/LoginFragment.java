package com.example.mosque_management_system.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.DashboardActivity;
import com.example.mosque_management_system.MainActivity;
import com.example.mosque_management_system.MosqueAccessActivity;
import com.example.mosque_management_system.R;
import com.example.mosque_management_system.admin.AdminDashboardActivity;
import com.example.mosque_management_system.api.AuthAPI;
import com.example.mosque_management_system.models.LoginRequest;
import com.example.mosque_management_system.models.LoginResponse;
import com.example.mosque_management_system.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView txtSignup;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        txtSignup = view.findViewById(R.id.txtSignup);

        loginButton.setOnClickListener(v -> loginUser());
        txtSignup.setOnClickListener(v -> {
            ((MainActivity) getActivity()).loadFragment(new SignupFragment());
        });

        return view;
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog loadingDialog = new Dialog(getActivity());
        loadingDialog.setContentView(R.layout.progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.show();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null);
        AuthAPI authAPI = retrofit.create(AuthAPI.class);

        Call<LoginResponse> call = authAPI.loginUser(new LoginRequest(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String token = response.body().getToken();
                    String fullName = response.body().getFullName();
                    boolean isAdmin = response.body().isAdmin();

                    SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("jwt_token", token);
                    editor.putString("fullName", fullName);
                    editor.putBoolean("isAdmin", isAdmin);
                    editor.apply();

                    Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    if (isAdmin) {
                        intent = new Intent(getActivity(), AdminDashboardActivity.class);
                    } else {
                        intent = new Intent(getActivity(), MosqueAccessActivity.class);
                    }
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(getActivity(), "Login Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
