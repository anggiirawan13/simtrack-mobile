package com.simple.tracking.admin.activity.shipper;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.ShipperAPIConfiguration;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateShipperActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialAutoCompleteTextView textInputUserIdCreate;
    private TextInputEditText textInputDeviceMappingCreate;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_shipper);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        textInputUserIdCreate = findViewById(R.id.textInputUserIdCreate);
        textInputDeviceMappingCreate = findViewById(R.id.textInputDeviceMappingCreate);

        ImageView btnBack = findViewById(R.id.btn_back_create_shipper);
        btnBack.setOnClickListener(this);

        CardView btnSave = findViewById(R.id.btn_save_create_shipper);
        btnSave.setOnClickListener(this);

        getUsers();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_create_shipper) finish();
        else if (view.getId() == R.id.btn_save_create_shipper) {
            String userName = textInputUserIdCreate.getText().toString();

            if (userList != null && !userList.isEmpty()) {
                User selectedUser = userList.stream()
                        .filter(user -> user.getFullname().equals(userName))
                        .findFirst()
                        .orElse(null);

                if (selectedUser != null) {
                    Shipper shipper = new Shipper.Builder()
                            .setUserId(selectedUser.getId())
                            .setDeviceMapping(Objects.requireNonNull(textInputDeviceMappingCreate.getText()).toString())
                            .build();

                    new AlertDialog.Builder(AdminCreateShipperActivity.this)
                            .setTitle("KONFIRMASI")
                            .setMessage("Apakah anda yakin ingin membuat data ini?")
                            .setPositiveButton("YES", (dialog, which) -> createShipper(shipper))
                            .setNegativeButton("NO", null)
                            .show();
                }
            }
        }
    }

    public void createShipper(Shipper shipper) {
        Call<BaseResponse<Shipper>> call = ShipperAPIConfiguration.getInstance().createShipper(shipper);
        call.enqueue(new Callback<BaseResponse<Shipper>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Shipper>> call, @NonNull Response<BaseResponse<Shipper>> response) {
                if (!response.isSuccessful())
                    new AlertDialog.Builder(AdminCreateShipperActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", (dialog, which) -> {})
                            .show();
                else {
                    BaseResponse<Shipper> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) finish();
                    else
                        new AlertDialog.Builder(AdminCreateShipperActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Shipper>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminCreateShipperActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", (dialog, which) -> {})
                        .show();
            }
        });
    }

    private void getUsers() {
        Call<BaseResponse<List<User>>> call = UserAPIConfiguration.getInstance().getUsers(null, false, null, null);
        call.enqueue(new Callback<BaseResponse<List<User>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<User>>> call, @NonNull Response<BaseResponse<List<User>>> response) {
                if (!response.isSuccessful())
                    new AlertDialog.Builder(AdminCreateShipperActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", (dialog, which) -> {})
                            .show();
                else {
                    BaseResponse<List<User>> baseResponse = response.body();
                    if (baseResponse == null || !baseResponse.isSuccess())
                        new AlertDialog.Builder(AdminCreateShipperActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", (dialog, which) -> {})
                                .show();
                    else {
                        userList = baseResponse.getData();
                        populateUserDropdown(userList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<User>>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminCreateShipperActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", (dialog, which) -> {})
                        .show();
            }
        });
    }

    private void populateUserDropdown(List<User> users) {
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, users);
        textInputUserIdCreate.setAdapter(adapter);

        textInputUserIdCreate.setOnClickListener(v -> {
            if (adapter.getCount() > 0) textInputUserIdCreate.showDropDown();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
    }
}
