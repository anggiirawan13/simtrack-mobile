package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LogoutActivity;
import com.simple.tracking.PreferenceManager;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.AdminActivity;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextInputEditText textInputFullnameProfileUser;
    private TextInputEditText textInputUsernameProfileUser;
    private TextInputEditText textInputPasswordProfileUser;
    private MaterialAutoCompleteTextView textInputRoleProfileUser;
    private TextInputEditText textInputAddressProfileUser;
    private TextInputEditText textInputSubDistrictProfileUser;
    private TextInputEditText textInputDistrictProfileUser;
    private TextInputEditText textInputCityProfileUser;
    private TextInputEditText textInputProvinceProfileUser;
    private TextInputEditText textInputPostalCodeProfileUser;
    private CardView btnUpdate;
    private CardView btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        textInputFullnameProfileUser = view.findViewById(R.id.textInputFullnameProfileUser);
        textInputUsernameProfileUser = view.findViewById(R.id.textInputUsernameProfileUser);
        textInputPasswordProfileUser = view.findViewById(R.id.textInputPasswordProfileUser);
        textInputRoleProfileUser = view.findViewById(R.id.textInputRoleProfileUser);
        textInputAddressProfileUser = view.findViewById(R.id.textInputAddressProfileUser);
        textInputSubDistrictProfileUser = view.findViewById(R.id.textInputSubDistrictProfileUser);
        textInputDistrictProfileUser = view.findViewById(R.id.textInputDistrictProfileUser);
        textInputCityProfileUser = view.findViewById(R.id.textInputCityProfileUser);
        textInputProvinceProfileUser = view.findViewById(R.id.textInputProvinceProfileUser);
        textInputPostalCodeProfileUser = view.findViewById(R.id.textInputPostalCodeProfileUser);
        btnUpdate = view.findViewById(R.id.btn_update_profile_user);
        btnSave = view.findViewById(R.id.btn_save_update_profile_user);

        String[] roles = {"Admin", "Commissioner", "Director", "Shipper"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, roles);
        textInputRoleProfileUser.setAdapter(adapter);

        textInputRoleProfileUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textInputRoleProfileUser.showDropDown();
            }
        });

        textInputRoleProfileUser.setOnClickListener(v -> textInputRoleProfileUser.showDropDown());

        btnUpdate.setOnClickListener(v -> {
            enableFields(true);

            btnUpdate.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if we are on the Home fragment
                // Navigate to logout
                if (btnUpdate.getVisibility() == View.GONE) {
                    enableFields(false);

                    btnUpdate.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(requireActivity(), LogoutActivity.class);
                    startActivity(intent);
                }
            }
        });

        PreferenceManager preferenceManager = new PreferenceManager(requireContext());
        int id = preferenceManager.getUserId();
        getUsers(id);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        enableFields(false);

        btnUpdate.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
    }

    public void getUsers(int id) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().getUser(id); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        User user = baseResponse.getData();
                        textInputFullnameProfileUser.setText(user.getFullname()); // Assuming getter is getFullname()
                        textInputUsernameProfileUser.setText(user.getUsername()); // Assuming getter is getUsername()
                        textInputPasswordProfileUser.setText(user.getPassword()); // Assuming getter is getPassword(), consider security implications
                        textInputRoleProfileUser.setText(user.getRole()); // Assuming getter is getRole()
                        textInputAddressProfileUser.setText(user.getAddress().getStreet()); // Assuming getter is getAddress()
                        textInputSubDistrictProfileUser.setText(user.getAddress().getSubDistrict()); // Assuming getter is getSubDistrict()
                        textInputDistrictProfileUser.setText(user.getAddress().getDistrict()); // Assuming getter is getDistrict()
                        textInputCityProfileUser.setText(user.getAddress().getCity()); // Assuming getter is getCity()
                        textInputProvinceProfileUser.setText(user.getAddress().getProvince()); // Assuming getter is getProvince()
                        textInputPostalCodeProfileUser.setText(user.getAddress().getPostalCode()); // Assuming getter is getPostalCode()
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
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch users: " + t.getMessage());
            }
        });
    }

    private void enableFields(boolean enable) {
        textInputFullnameProfileUser.setEnabled(enable);
        textInputUsernameProfileUser.setEnabled(enable);
        textInputPasswordProfileUser.setEnabled(enable);
        textInputAddressProfileUser.setEnabled(enable);
        textInputSubDistrictProfileUser.setEnabled(enable);
        textInputDistrictProfileUser.setEnabled(enable);
        textInputCityProfileUser.setEnabled(enable);
        textInputProvinceProfileUser.setEnabled(enable);
        textInputPostalCodeProfileUser.setEnabled(enable);
    }
}
