package com.simple.tracking.network;

import com.simple.tracking.model.Delivery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeliveryAPI {

    @GET("/api/deliveries")
    Call<BaseResponse<List<Delivery>>> getDeliveries();

    @POST("/api/deliveries")
    Call<BaseResponse<Delivery>> createDelivery(@Body Delivery delivery); // Changed to return a single Delivery

    @GET("/api/deliveries/{id}")
    Call<BaseResponse<Delivery>> getDelivery(@Path("id") int deliveryId); // Changed to return a single Delivery

    @PUT("/api/deliveries/{id}")
    Call<BaseResponse<Delivery>> updateDelivery(@Path("id") int deliveryId, @Body Delivery delivery); // Changed to return a single Delivery

    @DELETE("/api/deliveries/{id}")
    Call<BaseResponse<Void>> deleteDelivery(@Path("id") int deliveryId);
}