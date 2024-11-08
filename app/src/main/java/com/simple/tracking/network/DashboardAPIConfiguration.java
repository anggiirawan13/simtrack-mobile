package com.simple.tracking.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardAPIConfiguration {
    private static Retrofit retrofit = null;

    // Synchronized method to create and get the Retrofit instance
    public static DashboardAPI getInstance() {
        if (retrofit == null) {
            synchronized (DashboardAPIConfiguration.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Config.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit.create(DashboardAPI.class);
    }
}
