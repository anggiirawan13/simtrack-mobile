package com.simple.tracking.network

import com.simple.tracking.model.User
import retrofit2.Call
import retrofit2.http.GET

interface UserAPI {

    @GET("/user")
    fun getUsers(): Call<BaseResponse>

}