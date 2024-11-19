package com.simple.tracking.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.LogoutActivity;
import com.simple.tracking.PreferenceManager;
import com.simple.tracking.R;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText textInputFullnameProfileUser, textInputUsernameProfileUser,
            textInputPasswordProfileUser, textInputAddressProfileUser, textInputSubDistrictProfileUser,
            textInputDistrictProfileUser, textInputCityProfileUser, textInputProvinceProfileUser,
            textInputPostalCodeProfileUser, textInputWhatsappProfileUser;
    private MaterialAutoCompleteTextView textInputRoleProfileUser;
    private CardView btnUpdate, btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

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
        textInputWhatsappProfileUser = view.findViewById(R.id.textInputWhatsappProfileUser);
        btnSave = view.findViewById(R.id.btn_save_update_profile_user);
        btnUpdate = view.findViewById(R.id.btn_update_profile_user);

        String[] roles = {"Admin", "Commissioner", "Director", "Shipper"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, roles);
        textInputRoleProfileUser.setAdapter(adapter);

        textInputRoleProfileUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) textInputRoleProfileUser.showDropDown();
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

        btnSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(requireActivity())) {
            LocationChecker.showLocationDisabledDialog(requireActivity());
        }

        enableFields(false);

        btnUpdate.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
    }

    public void getUsers(int id) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().getUser(id);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        User user = baseResponse.getData();
                        textInputFullnameProfileUser.setText(user.getFullname());
                        textInputUsernameProfileUser.setText(user.getUsername());
                        textInputPasswordProfileUser.setText(user.getPassword());
                        textInputRoleProfileUser.setText(user.getRole());
                        textInputWhatsappProfileUser.setText(user.getAddress().getWhatsapp());
                        textInputAddressProfileUser.setText(user.getAddress().getStreet());
                        textInputSubDistrictProfileUser.setText(user.getAddress().getSubDistrict());
                        textInputDistrictProfileUser.setText(user.getAddress().getDistrict());
                        textInputCityProfileUser.setText(user.getAddress().getCity());
                        textInputProvinceProfileUser.setText(user.getAddress().getProvince());
                        textInputPostalCodeProfileUser.setText(user.getAddress().getPostalCode());
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void enableFields(boolean enable) {
        textInputWhatsappProfileUser.setEnabled(enable);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_update_profile_user) {
            Address address = new Address.Builder()
                    .setWhatsapp(Objects.requireNonNull(textInputWhatsappProfileUser.getText()).toString())
                    .setStreet(Objects.requireNonNull(textInputAddressProfileUser.getText()).toString())
                    .setSubDistrict(Objects.requireNonNull(textInputSubDistrictProfileUser.getText()).toString())
                    .setDistrict(Objects.requireNonNull(textInputDistrictProfileUser.getText()).toString())
                    .setCity(Objects.requireNonNull(textInputCityProfileUser.getText()).toString())
                    .setProvince(Objects.requireNonNull(textInputProvinceProfileUser.getText()).toString())
                    .setPostalCode(Objects.requireNonNull(textInputPostalCodeProfileUser.getText()).toString())
                    .build();

            PreferenceManager preferenceManager = new PreferenceManager(requireContext());
            int id = preferenceManager.getUserId();

            User user = new User.Builder()
                    .setId(id)
                    .setFullname(Objects.requireNonNull(textInputFullnameProfileUser.getText()).toString())
                    .setUsername(Objects.requireNonNull(textInputUsernameProfileUser.getText()).toString())
                    .setPassword(Objects.requireNonNull(textInputPasswordProfileUser.getText()).toString())
                    .setRole(Objects.requireNonNull(textInputRoleProfileUser.getText()).toString())
                    .setAddress(address)
                    .build();

            new AlertDialog.Builder(requireContext())
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin membuat data ini?")
                    .setPositiveButton("YES", (dialog, which) -> updateProfile(user))
                    .setNegativeButton("NO", null)
                    .show();
        }
    }

    public void updateProfile(User user) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().updateUser(user.getId(), user);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                BaseResponse<User> baseResponse = response.body();
                assert baseResponse != null;
                if (!baseResponse.isSuccess())
                    new AlertDialog.Builder(requireContext())
                            .setTitle("GAGAL")
                            .setMessage(baseResponse.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                else {
                    enableFields(false);
                    new AlertDialog.Builder(requireContext())
                            .setTitle("BERHASIL")
                            .setMessage("Profile berhasil diubah.")
                            .setPositiveButton("OK", (dialog, which) -> {
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}
