package com.simple.tracking.network;

import com.simple.tracking.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface UserAPI {

    @GET("/api/users")
    Call<BaseResponse<List<User>>> getUsers();

    @POST("/api/users")
    Call<BaseResponse<User>> createUser(@Body User user); // Changed to return a single User

    @GET("/api/users/{id}")
    Call<BaseResponse<User>> getUser(@Path("id") int userId); // Changed to return a single User

    @PUT("/api/users/{id}")
    Call<BaseResponse<User>> updateUser(@Path("id") int userId, @Body User user); // Changed to return a single User

    @DELETE("/api/users/{id}")
    Call<BaseResponse<Void>> deleteUser(@Path("id") int userId);
}
