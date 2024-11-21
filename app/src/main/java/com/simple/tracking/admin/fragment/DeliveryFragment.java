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
import android.webkit.PermissionRequest;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.PreferenceManager;
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
import java.util.Objects;

public class DeliveryFragment extends Fragment {

    private DeliveryAdapter deliveryAdapter;
    private RecyclerView recyclerView;
    private TextInputEditText searchInput;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int PAGE_SIZE = 10;
    private final Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_delivery, container, false);

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        recyclerView = view.findViewById(R.id.recyclerViewDelivery);
        searchInput = view.findViewById(R.id.search_input_delivery);
        MaterialButton btnSearch = view.findViewById(R.id.btn_search_delivery);
        CardView btnAddDelivery = view.findViewById(R.id.btn_add_delivery);

        PreferenceManager preferenceManager = new PreferenceManager(requireContext());
        if (preferenceManager.isAdmin()) btnAddDelivery.setVisibility(View.VISIBLE);

        btnAddDelivery.setOnClickListener(v -> {
            Intent deliveryCreate = new Intent(requireContext(), AdminCreateDeliveryDetailActivity.class);
            deliveryCreate.putExtra("MENU_NAME", "Delivery");
            startActivity(deliveryCreate);
        });

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
                        getDeliveries(null);
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            currentPage = 1;
            isLastPage = false;
            isLoading = false;

            if (deliveryAdapter != null) {
                deliveryAdapter.clearDeliveries();
            }

            if (searchInput == null || searchInput.toString().trim().isEmpty()) {
                getDeliveries(null);
            } else {
                getDeliveries(Objects.requireNonNull(searchInput.getText()).toString());
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
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

                        if (deliveryAdapter != null) {
                            deliveryAdapter.clearDeliveries();
                        }

                        getDeliveries(null);
                    }
                };

                searchHandler.postDelayed(searchRunnable, 1000);
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

        getDeliveries(null);
    }

    private void getDeliveries(String query) {
        if (isLoading || isLastPage) return;

        isLoading = true;

        Call<BaseResponse<List<Delivery>>> call;

        PreferenceManager preferenceManager = new PreferenceManager(requireContext());
        if (preferenceManager.isAdmin()) {
            call = DeliveryAPIConfiguration.getInstance().getDeliveries(query, currentPage, PAGE_SIZE);
        } else {
            call = DeliveryAPIConfiguration.getInstance().getDeliveriesShipper(preferenceManager.getUserId(), query, currentPage, PAGE_SIZE);
        }

        call.enqueue(new Callback<BaseResponse<List<Delivery>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<Delivery>>> call, @NonNull Response<BaseResponse<List<Delivery>>> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    BaseResponse<List<Delivery>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        List<Delivery> deliveries = baseResponse.getData();

                        if (currentPage == 1) {
                            deliveryAdapter = new DeliveryAdapter(deliveries);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(deliveryAdapter);
                        } else {
                            deliveryAdapter.addDeliveries(deliveries);
                        }

                        if (deliveries != null && deliveries.size() < PAGE_SIZE) isLastPage = true;
                        else currentPage++;
                    } else {
                        isLastPage = true;
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<Delivery>>> call, @NonNull Throwable t) {
                isLoading = false;
                Log.e("API Error", "Failed to fetch deliveries: " + t.getMessage());
            }
        });
    }
}
