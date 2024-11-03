package com.simple.tracking.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAPIConfiguration {
    private static final String BASE_URL = "https://super-warthog-wondrous.ngrok-free.app/";
    private static Retrofit retrofit = null;

    // Synchronized method to create and get the Retrofit instance
    public static LoginAPI getInstance() {
        if (retrofit == null) {
            synchronized (LoginAPIConfiguration.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit.create(LoginAPI.class);
    }
}
