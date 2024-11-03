package com.simple.tracking.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPIConfiguration {
    private static final String BASE_URL = "https://simtrack.up.railway.app/";
    private static Retrofit retrofit = null;

    // Synchronized method to create and get the Retrofit instance
    public static UserAPI getInstance() {
        if (retrofit == null) {
            synchronized (UserAPIConfiguration.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit.create(UserAPI.class);
    }
}
