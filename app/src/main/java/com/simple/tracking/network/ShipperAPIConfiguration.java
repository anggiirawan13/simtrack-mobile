package com.simple.tracking.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShipperAPIConfiguration {
    private static final String BASE_URL = "https://simtrack-backend.up.railway.app/";
    private static Retrofit retrofit = null;

    // Synchronized method to create and get the Retrofit instance
    public static ShipperAPI getInstance() {
        if (retrofit == null) {
            synchronized (ShipperAPIConfiguration.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Config.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit.create(ShipperAPI.class);
    }
}
