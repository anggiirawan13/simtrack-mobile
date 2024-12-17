package com.simple.tracking.network;

import com.simple.tracking.model.Auth;
import com.simple.tracking.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoginAPI {

    @Headers("Content-Type: application/json")
    @POST("/api/auth/login")
    Call<BaseResponse<User>> login(@Body Auth auth);
}
