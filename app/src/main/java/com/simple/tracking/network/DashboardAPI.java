package com.simple.tracking.network;

import com.simple.tracking.model.Dashboard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DashboardAPI {

    @GET("/api/dashboard")
    Call<BaseResponse<Dashboard>> getDashboard();

}
