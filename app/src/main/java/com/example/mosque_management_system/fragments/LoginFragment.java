package com.example.mosque_management_system.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.*;
import com.example.mosque_management_system.admin.AdminDashboardActivity;
import com.example.mosque_management_system.api.AuthAPI;
import com.example.mosque_management_system.models.*;
import com.example.mosque_management_system.network.RetrofitClient;
import com.example.mosque_management_system.utils.PreferenceHelper;

import retrofit2.*;


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

                    PreferenceHelper.saveToken(getActivity(), token);
                    PreferenceHelper.saveFullName(getActivity(), fullName);
                    PreferenceHelper.saveIsAdmin(getActivity(), isAdmin);
                    PreferenceHelper.setLoggedIn(getActivity(), true);

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
