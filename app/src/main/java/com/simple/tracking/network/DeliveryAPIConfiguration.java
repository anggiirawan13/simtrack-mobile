package com.simple.tracking.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeliveryAPIConfiguration {
    private static final String BASE_URL = "https://simtrack.up.railway.app/";
    private static Retrofit retrofit = null;

    // Synchronized method to create and get the Retrofit instance
    public static DeliveryAPI getInstance() {
        if (retrofit == null) {
            synchronized (DeliveryAPIConfiguration.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit.create(DeliveryAPI.class);
    }
}
