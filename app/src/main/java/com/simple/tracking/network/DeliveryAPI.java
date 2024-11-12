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
import retrofit2.http.Query;

public interface DeliveryAPI {

    @GET("/api/deliveries")
    Call<BaseResponse<List<Delivery>>> getDeliveries(
            @Query("q") String q,
            @Query("paginate") boolean paginate,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @GET("/api/deliveries/filter/status")
    Call<BaseResponse<List<Delivery>>> getDeliveriesByStatus(
            @Query("q") String q,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @POST("/api/deliveries")
    Call<BaseResponse<Delivery>> createDelivery(@Body Delivery delivery); // Changed to return a single Delivery

    @GET("/api/deliveries/{id}")
    Call<BaseResponse<Delivery>> getDelivery(@Path("id") int deliveryId); // Changed to return a single Delivery

    @PUT("/api/deliveries/{id}")
    Call<BaseResponse<Delivery>> updateDelivery(@Path("id") int deliveryId, @Body Delivery delivery); // Changed to return a single Delivery

    @DELETE("/api/deliveries/{id}")
    Call<BaseResponse<Void>> deleteDelivery(@Path("id") int deliveryId);

    @GET("/api/deliveries/generate")
    Call<BaseResponse<String>> getDeliveryNumber();
}
