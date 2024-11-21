package com.simple.tracking.admin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.PreferenceManager;
import com.simple.tracking.R;
import com.simple.tracking.model.Dashboard;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DashboardAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView totalDiproses;
    private TextView totalDikirim;
    private TextView totalDiterima;
    private TextView totalKeseluruhan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        totalDiproses = view.findViewById(R.id.totalDiproses);
        totalDikirim = view.findViewById(R.id.totalDikirim);
        totalDiterima = view.findViewById(R.id.totalDiterima);
        totalKeseluruhan = view.findViewById(R.id.totalKeseluruhan);

        getDashboard();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }
    }

    public void getDashboard() {
        Call<BaseResponse<Dashboard>> call;

        PreferenceManager preferenceManager = new PreferenceManager(requireContext());
        if (preferenceManager.isAdmin()) {
            call = DashboardAPIConfiguration.getInstance().getDashboard();
        } else {
            call = DashboardAPIConfiguration.getInstance().getDashboardShipper(preferenceManager.getUserId());
        }

        call.enqueue(new Callback<BaseResponse<Dashboard>>() {
            @Override
            public void onResponse(Call<BaseResponse<Dashboard>> call, Response<BaseResponse<Dashboard>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Dashboard> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        Dashboard dashboard = baseResponse.getData();
                        totalDiproses.setText(String.valueOf(dashboard.getProses()));
                        totalDikirim.setText(String.valueOf(dashboard.getKirim()));
                        totalDiterima.setText(String.valueOf(dashboard.getTerima()));
                        totalKeseluruhan.setText(String.valueOf(dashboard.getTotal()));
                    } else {
                        Toast.makeText(requireContext(), "GAGAL", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(requireContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Dashboard>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch dashboards: " + t.getMessage());
            }
        });
    }
}
