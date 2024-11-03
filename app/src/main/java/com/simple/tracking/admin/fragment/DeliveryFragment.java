package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_delivery, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDelivery);
        btnAddDelivery = view.findViewById(R.id.btn_add_delivery);

        btnAddDelivery.setOnClickListener(v -> {
            Intent deliveryCreate = new Intent(requireContext(), AdminCreateDeliveryDetailActivity.class);
            deliveryCreate.putExtra("MENU_NAME", "Delivery");
            startActivity(deliveryCreate);
        });

        // Fetch deliveries from the API
        getDeliveries();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDeliveries(); // Fetch deliveries whenever the fragment is resumed
    }

    private void getDeliveries() {
        Call<BaseResponse<List<Delivery>>> call = DeliveryAPIConfiguration.getInstance().getDeliveries();
        call.enqueue(new Callback<BaseResponse<List<Delivery>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Delivery>>> call, Response<BaseResponse<List<Delivery>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<Delivery>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        if (baseResponse.getData().isEmpty()) {
                            Log.d("API Info", "No deliveries found");
                            Toast.makeText(getContext(), "No deliveries found", Toast.LENGTH_SHORT).show();
                        } else {
                            deliveryAdapter = new DeliveryAdapter(baseResponse.getData());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(deliveryAdapter);
                        }
                    } else {
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Delivery>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch deliveries: " + t.getMessage());
            }
        });
    }
}
