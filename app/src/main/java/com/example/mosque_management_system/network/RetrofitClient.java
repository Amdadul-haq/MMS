package com.example.mosque_management_system.network;

import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static final String BASE_URL = "https://mosquemanagementsystem.onrender.com";

    public static Retrofit getRetrofitInstance(String token) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (token != null) {
            clientBuilder.addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization","Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            });
        }

        OkHttpClient client = clientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
