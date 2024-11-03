package com.simple.tracking.network;

import com.simple.tracking.model.Shipper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ShipperAPI {

    @GET("/api/shippers")
    Call<BaseResponse<List<Shipper>>> getShippers();

    @POST("/api/shippers")
    Call<BaseResponse<Shipper>> createShipper(@Body Shipper shipper); // Changed to return a single Shipper

    @GET("/api/shippers/{id}")
    Call<BaseResponse<Shipper>> getShipper(@Path("id") int shipperId); // Changed to return a single Shipper

    @PUT("/api/shippers/{id}")
    Call<BaseResponse<Shipper>> updateShipper(@Path("id") int shipperId, @Body Shipper shipper); // Changed to return a single Shipper

    @DELETE("/api/shippers/{id}")
    Call<BaseResponse<Void>> deleteShipper(@Path("id") int shipperId);
}
