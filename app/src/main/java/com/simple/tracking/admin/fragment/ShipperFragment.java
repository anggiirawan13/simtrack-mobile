package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.shipper.AdminCreateShipperActivity;
import com.simple.tracking.admin.adapter.ShipperAdapter;
import com.simple.tracking.admin.adapter.ShipperAdapter;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.ShipperAPIConfiguration;
import com.simple.tracking.network.ShipperAPIConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.Objects;

public class ShipperFragment extends Fragment {

    private ShipperAdapter shipperAdapter;
    private RecyclerView recyclerView;
    private CardView btnAddShipper;
    private MaterialButton btnSearchShipper;
    private TextInputEditText textInputSearchShipper;
    private final Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int PAGE_SIZE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_shipper, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewShipper);
        btnAddShipper = view.findViewById(R.id.btn_add_shipper);
        btnSearchShipper = view.findViewById(R.id.btn_search_shipper);
        textInputSearchShipper = view.findViewById(R.id.textInputSearchShipper);

        btnAddShipper.setOnClickListener(v -> {
            Intent shipperCreate = new Intent(requireContext(), AdminCreateShipperActivity.class);
            shipperCreate.putExtra("MENU_NAME", "Shipper");
            startActivity(shipperCreate);
        });

        btnSearchShipper.setOnClickListener(v -> {
            currentPage = 1;
            isLastPage = false;
            isLoading = false;

            // Clear current data in the adapter before performing new search
            if (shipperAdapter != null) {
                shipperAdapter.clearShippers();
            }

            getShippers(Objects.requireNonNull(textInputSearchShipper.getText()).toString());
        });

        textInputSearchShipper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    if (s.toString().isEmpty()) {
                        currentPage = 1;
                        isLastPage = false;
                        isLoading = false;

                        if (shipperAdapter != null) {
                            shipperAdapter.clearShippers();
                        }

                        getShippers(null); // Fetch without a query to get all users
                    }
                };

                searchHandler.postDelayed(searchRunnable, 1000); // 1-second delay
            }
        });

        // Fetch shippers from the API
        getShippers(null);

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
                        getShippers(null); // Load the next page
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        isLastPage = false;
        getShippers(null); // Fetch first page of shippers
    }

    private void getShippers(String query) {
        if (isLoading || isLastPage) return; // Prevent duplicate requests

        isLoading = true;
        Call<BaseResponse<List<Shipper>>> call = ShipperAPIConfiguration.getInstance().getShippers(query, true, currentPage, PAGE_SIZE);
        call.enqueue(new Callback<BaseResponse<List<Shipper>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Shipper>>> call, Response<BaseResponse<List<Shipper>>> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    BaseResponse<List<Shipper>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        List<Shipper> shippers = baseResponse.getData();

                        if (currentPage == 1) {
                            // For the first page, set up the adapter
                            shipperAdapter = new ShipperAdapter(shippers);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(shipperAdapter);
                        } else {
                            // Add new data to existing list on subsequent pages
                            shipperAdapter.addShippers(shippers);
                        }

                        // Check if this is the last page
                        if (shippers.size() < PAGE_SIZE) {
                            isLastPage = true;
                        } else {
                            currentPage++; // Load next page on next scroll
                        }
                    } else {
                        isLastPage = true; // No more data
                        Log.d("API Info", "No more shippers to load");
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Shipper>>> call, Throwable t) {
                isLoading = false;
                Log.e("API Error", "Failed to fetch shippers: " + t.getMessage());
            }
        });
    }
}
