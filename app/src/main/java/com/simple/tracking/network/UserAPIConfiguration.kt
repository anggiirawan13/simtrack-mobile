package com.simple.tracking.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserAPIConfiguration {

    private const val BASE_URL = "https://super-warthog-wondrous.ngrok-free.app/"

    val instance: UserAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }

}