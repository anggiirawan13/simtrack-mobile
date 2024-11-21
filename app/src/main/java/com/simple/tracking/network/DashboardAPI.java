package com.simple.tracking.network;

import com.simple.tracking.model.Dashboard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DashboardAPI {

    @GET("/api/dashboard")
    Call<BaseResponse<Dashboard>> getDashboard();

    @GET("/api/dashboard/{id}")
    Call<BaseResponse<Dashboard>> getDashboardShipper(@Path("id") int userId);

}
