package com.example.mosque_management_system.fragments;

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

import com.example.mosque_management_system.MainActivity;
import com.example.mosque_management_system.R;
import com.example.mosque_management_system.api.AuthAPI;
import com.example.mosque_management_system.models.SignupRequest;
import com.example.mosque_management_system.models.SignupResponse;
import com.example.mosque_management_system.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupFragment extends Fragment {

    private EditText fullNameInput, emailInput, passwordInput;
    private Button signupButton;
    private TextView loginText;

    public SignupFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        fullNameInput = view.findViewById(R.id.fullNameInput);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        signupButton = view.findViewById(R.id.signupButton);
        loginText = view.findViewById(R.id.txtLogin);

        signupButton.setOnClickListener(v -> signupUser());

        // Navigate to Login Page
        loginText.setOnClickListener(v -> ((MainActivity) getActivity()).loadFragment(new LoginFragment()));

        return view;
    }

    private void signupUser() {
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null);
        AuthAPI authAPI = retrofit.create(AuthAPI.class);

        Call<SignupResponse> call = authAPI.signupUser(new SignupRequest(fullName, email, password));
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Signup Successful!", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).loadFragment(new LoginFragment());
                } else {
                    Toast.makeText(getActivity(), "Signup Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
