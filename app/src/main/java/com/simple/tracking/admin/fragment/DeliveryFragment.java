package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.delivery.AdminCreateDeliveryDetailActivity;
import com.simple.tracking.admin.adapter.DeliveryAdapter;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class DeliveryFragment extends Fragment {

    private DeliveryAdapter deliveryAdapter;
    private RecyclerView recyclerView;
    private CardView btnAddDelivery;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int PAGE_SIZE = 10; // Adjust as needed

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_delivery, container, false);

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        recyclerView = view.findViewById(R.id.recyclerViewDelivery);
        btnAddDelivery = view.findViewById(R.id.btn_add_delivery);

        btnAddDelivery.setOnClickListener(v -> {
            Intent deliveryCreate = new Intent(requireContext(), AdminCreateDeliveryDetailActivity.class);
            deliveryCreate.putExtra("MENU_NAME", "Delivery");
            startActivity(deliveryCreate);
        });

        // Fetch deliveries from the API
        getDeliveries(null);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        getDeliveries(null); // Load the next page
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        currentPage = 1;
        isLastPage = false;

        getDeliveries(null); // Fetch first page of deliveries
    }

    private void getDeliveries(String query) {
        if (isLoading || isLastPage) return; // Prevent duplicate requests

        isLoading = true;
        Call<BaseResponse<List<Delivery>>> call = DeliveryAPIConfiguration.getInstance().getDeliveries(query, true, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<BaseResponse<List<Delivery>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Delivery>>> call, Response<BaseResponse<List<Delivery>>> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    BaseResponse<List<Delivery>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        List<Delivery> deliveries = baseResponse.getData();

                        if (currentPage == 1) {
                            // For the first page, set up the adapter
                            deliveryAdapter = new DeliveryAdapter(deliveries);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(deliveryAdapter);
                        } else {
                            // Add new data to existing list on subsequent pages
                            deliveryAdapter.addDeliveries(deliveries);
                        }

                        // Check if this is the last page
                        if (deliveries.size() < PAGE_SIZE) {
                            isLastPage = true;
                        } else {
                            currentPage++; // Load next page on next scroll
                        }
                    } else {
                        isLastPage = true; // No more data
                        Log.d("API Info", "No more deliveries to load");
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Delivery>>> call, Throwable t) {
                isLoading = false;
                Log.e("API Error", "Failed to fetch deliveries: " + t.getMessage());
            }
        });
    }
}
