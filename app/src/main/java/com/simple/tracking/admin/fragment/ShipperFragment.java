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
import com.simple.tracking.admin.activity.shipper.AdminCreateShipperActivity;
import com.simple.tracking.admin.adapter.ShipperAdapter;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.ShipperAPIConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ShipperFragment extends Fragment {

    private ShipperAdapter shipperAdapter;
    private RecyclerView recyclerView;
    private CardView btnAddShipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_shipper, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewShipper);
        btnAddShipper = view.findViewById(R.id.btn_add_shipper);

        btnAddShipper.setOnClickListener(v -> {
            Intent shipperCreate = new Intent(requireContext(), AdminCreateShipperActivity.class);
            shipperCreate.putExtra("MENU_NAME", "Shipper");
            startActivity(shipperCreate);
        });

        // Fetch shippers from the API
        getShippers();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getShippers(); // Fetch shippers whenever the fragment is resumed
    }

    private void getShippers() {
        Call<BaseResponse<List<Shipper>>> call = ShipperAPIConfiguration.getInstance().getShippers();
        call.enqueue(new Callback<BaseResponse<List<Shipper>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Shipper>>> call, Response<BaseResponse<List<Shipper>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<Shipper>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        if (baseResponse.getData().isEmpty()) {
                            Log.d("API Info", "No shippers found");
                            Toast.makeText(getContext(), "No shippers found", Toast.LENGTH_SHORT).show();
                        } else {
                            shipperAdapter = new ShipperAdapter(baseResponse.getData());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(shipperAdapter);
                        }
                    } else {
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Shipper>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch shippers: " + t.getMessage());
            }
        });
    }
}
