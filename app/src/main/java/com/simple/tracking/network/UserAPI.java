package com.simple.tracking.network;

import com.simple.tracking.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface UserAPI {

    @GET("/api/users")
    Call<BaseResponse<List<User>>> getUsers(
            @Query("q") String q,
            @Query("paginate") boolean paginate,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @POST("/api/users")
    Call<BaseResponse<User>> createUser(@Body User user);

    @GET("/api/users/{id}")
    Call<BaseResponse<User>> getUser(@Path("id") int userId);

    @PUT("/api/users/{id}")
    Call<BaseResponse<User>> updateUser(@Path("id") int userId, @Body User user);

    @DELETE("/api/users/{id}")
    Call<BaseResponse<Void>> deleteUser(@Path("id") int userId);
}
