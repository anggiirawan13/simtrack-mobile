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
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewShipperActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialAutoCompleteTextView textInputUserIdView;
    private TextInputEditText textInputDeviceMappingView;
    private CardView btnDelete, btnSave, btnUpdate;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_shipper);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        textInputUserIdView = findViewById(R.id.textInputUserIdView);
        textInputDeviceMappingView = findViewById(R.id.textInputDeviceMappingView);

        ImageView btnBack = findViewById(R.id.btn_back_view_shipper);
        btnBack.setOnClickListener(this);

        btnDelete = findViewById(R.id.btn_delete_view_shipper);
        btnDelete.setOnClickListener(this);

        btnSave = findViewById(R.id.btn_save_view_shipper);
        btnSave.setOnClickListener(this);

        btnUpdate = findViewById(R.id.btn_update_view_shipper);
        btnUpdate.setOnClickListener(this);

        getUsers();
    }

    public void getShippers(int id) {
        Call<BaseResponse<Shipper>> call = ShipperAPIConfiguration.getInstance().getShipper(id);
        call.enqueue(new Callback<BaseResponse<Shipper>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Shipper>> call, @NonNull Response<BaseResponse<Shipper>> response) {
                if (!response.isSuccessful()) {
                    new AlertDialog.Builder(AdminViewShipperActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    BaseResponse<Shipper> baseResponse = response.body();
                    if (baseResponse == null || !baseResponse.isSuccess()) {
                        new AlertDialog.Builder(AdminViewShipperActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        Shipper shipper = baseResponse.getData();
                        textInputDeviceMappingView.setText(shipper.getDeviceMapping());

                        if (userList != null && !userList.isEmpty())
                            userList.forEach(user -> {
                                if (user.getId() == shipper.getUserId()) textInputUserIdView.setText(user.toString(), false);
                            });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Shipper>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewShipperActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void enableFields() {
        textInputUserIdView.setEnabled(true);
        textInputDeviceMappingView.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int shipperId = getIntent().getIntExtra("SHIPPER_ID", -1);

        if (view.getId() == R.id.btn_back_view_shipper) finish();
        else if (view.getId() == R.id.btn_delete_view_shipper)
            new AlertDialog.Builder(AdminViewShipperActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin menghapus data ini?")
                    .setPositiveButton("YES", (dialog, which) -> deleteShipper(shipperId))
                    .setNegativeButton("NO", null)
                    .show();
        else if (view.getId() == R.id.btn_update_view_shipper) {
            enableFields();

            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.btn_save_view_shipper) {
            String userName = textInputUserIdView.getText().toString();

            AtomicReference<User> selectedUser = new AtomicReference<>();
            if (userList != null && !userList.isEmpty())
                userList.forEach(user -> {
                    if (user.getFullname().equals(userName)) selectedUser.set(user);
                });

            if (selectedUser.get() == null) {
                new AlertDialog.Builder(AdminViewShipperActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                Shipper shipper = new Shipper.Builder()
                        .setId(shipperId)
                        .setUserId(selectedUser.get().getId())
                        .setDeviceMapping(Objects.requireNonNull(textInputDeviceMappingView.getText()).toString())
                        .build();

                new AlertDialog.Builder(AdminViewShipperActivity.this)
                        .setTitle("KONFIRMASI")
                        .setMessage("Apakah anda yakin ingin mengubah data ini?")
                        .setPositiveButton("YES", (dialog, which) -> updateShipper(shipper))
                        .setNegativeButton("NO", null)
                        .show();
            }
        }
    }

    public void updateShipper(Shipper shipper) {
        Call<BaseResponse<Shipper>> call = ShipperAPIConfiguration.getInstance().updateShipper(shipper.getId(), shipper);
        call.enqueue(new Callback<BaseResponse<Shipper>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Shipper>> call, @NonNull Response<BaseResponse<Shipper>> response) {
                if (!response.isSuccessful())
                    new AlertDialog.Builder(AdminViewShipperActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", (dialog, which) -> {})
                            .show();
                else {
                    BaseResponse<Shipper> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) finish();
                    else
                        new AlertDialog.Builder(AdminViewShipperActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Shipper>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewShipperActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", (dialog, which) -> {})
                        .show();
            }
        });
    }

    public void deleteShipper(int id) {
        Call<BaseResponse<Void>> call = ShipperAPIConfiguration.getInstance().deleteShipper(id);
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Void>> call, @NonNull Response<BaseResponse<Void>> response) {
                BaseResponse<Void> baseResponse = response.body();
                assert baseResponse != null;
                if (!baseResponse.isSuccess())
                    new AlertDialog.Builder(AdminViewShipperActivity.this)
                            .setTitle("GAGAL")
                            .setMessage(baseResponse.getMessage())
                            .setPositiveButton("OK", (dialog, which) -> {})
                            .show();
                else {
                    if (baseResponse.isSuccess()) finish();
                    else
                        new AlertDialog.Builder(AdminViewShipperActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Void>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewShipperActivity.this)
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
                    new AlertDialog.Builder(AdminViewShipperActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", (dialog, which) -> {})
                            .show();
                else {
                    BaseResponse<List<User>> baseResponse = response.body();
                    if (baseResponse == null || !baseResponse.isSuccess())
                        new AlertDialog.Builder(AdminViewShipperActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", (dialog, which) -> {})
                                .show();
                    else {
                        userList = baseResponse.getData();
                        populateUserDropdown(userList);

                        int id = getIntent().getIntExtra("SHIPPER_ID", 1);
                        getShippers(id);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<User>>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewShipperActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", (dialog, which) -> {})
                        .show();
            }
        });
    }

    private void populateUserDropdown(List<User> users) {
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, users);
        textInputUserIdView.setAdapter(adapter);

        textInputUserIdView.setOnClickListener(v -> {
            if (adapter.getCount() > 0) textInputUserIdView.showDropDown();
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
