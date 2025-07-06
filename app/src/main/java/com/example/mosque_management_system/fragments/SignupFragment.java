package com.example.mosque_management_system.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import com.example.mosque_management_system.*;
import com.example.mosque_management_system.api.AuthAPI;
import com.example.mosque_management_system.models.*;
import com.example.mosque_management_system.network.RetrofitClient;

import org.json.JSONObject;

import retrofit2.*;


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
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("SIGNUP_ERROR", errorBody);

                        // Try to parse JSON error and show message
                        String errorMessage = "Signup Failed!";
                        try {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            if (jsonObject.has("message")) {
                                errorMessage = "Signup Failed: " + jsonObject.getString("message");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Signup Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
