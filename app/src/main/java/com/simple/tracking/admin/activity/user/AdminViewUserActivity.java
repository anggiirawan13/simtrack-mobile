package com.simple.tracking.admin.activity.user;

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
import com.simple.tracking.AdminChecker;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText textInputFullnameView, textInputUsernameView, textInputPasswordView,
            textInputAddressView, textInputSubDistrictView, textInputDistrictView,
            textInputCityView, textInputProvinceView, textInputPostalCodeView, textInputWhatsappView;
    private MaterialAutoCompleteTextView textInputRoleView;
    private CardView btnDelete, btnUpdate, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        textInputFullnameView = findViewById(R.id.textInputFullnameView);
        textInputUsernameView = findViewById(R.id.textInputUsernameView);
        textInputPasswordView = findViewById(R.id.textInputPasswordView);
        textInputRoleView = findViewById(R.id.textInputRoleView);
        textInputAddressView = findViewById(R.id.textInputAddressView);
        textInputSubDistrictView = findViewById(R.id.textInputSubDistrictView);
        textInputDistrictView = findViewById(R.id.textInputDistrictView);
        textInputCityView = findViewById(R.id.textInputCityView);
        textInputProvinceView = findViewById(R.id.textInputProvinceView);
        textInputPostalCodeView = findViewById(R.id.textInputPostalCodeView);
        textInputWhatsappView = findViewById(R.id.textInputWhatsappViewUser);
        btnDelete = findViewById(R.id.btn_delete_user_view);
        btnUpdate = findViewById(R.id.btn_update_user_view);
        btnSave = findViewById(R.id.btn_save_update_user);

        int id = getIntent().getIntExtra("USER_ID", -1);
        getUsers(id);

        if (!AdminChecker.isAdmin(this, id)) {
            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        }

        ImageView btnBack = findViewById(R.id.btn_back_view_user);
        btnBack.setOnClickListener(this);

        btnDelete.setOnClickListener(this);

        btnSave = findViewById(R.id.btn_save_update_user);
        btnSave.setOnClickListener(this);

        btnUpdate.setOnClickListener(this);
    }

    public void getUsers(int id) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().getUser(id);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (!response.isSuccessful())
                    new AlertDialog.Builder(AdminViewUserActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                else {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse == null || !baseResponse.isSuccess())
                        new AlertDialog.Builder(AdminViewUserActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    else {
                        User user = baseResponse.getData();
                        textInputFullnameView.setText(user.getFullname());
                        textInputUsernameView.setText(user.getUsername());
                        textInputPasswordView.setText(user.getPassword());
                        textInputWhatsappView.setText(user.getAddress().getWhatsapp());
                        textInputRoleView.setText(user.getRole());
                        textInputAddressView.setText(user.getAddress().getStreet());
                        textInputSubDistrictView.setText(user.getAddress().getSubDistrict());
                        textInputDistrictView.setText(user.getAddress().getDistrict());
                        textInputCityView.setText(user.getAddress().getCity());
                        textInputProvinceView.setText(user.getAddress().getProvince());
                        textInputPostalCodeView.setText(user.getAddress().getPostalCode());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewUserActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void enableFields() {
        textInputFullnameView.setEnabled(true);
        textInputUsernameView.setEnabled(true);
        textInputPasswordView.setEnabled(true);
        textInputWhatsappView.setEnabled(true);
        textInputRoleView.setEnabled(true);
        textInputAddressView.setEnabled(true);
        textInputSubDistrictView.setEnabled(true);
        textInputDistrictView.setEnabled(true);
        textInputCityView.setEnabled(true);
        textInputProvinceView.setEnabled(true);
        textInputPostalCodeView.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int userId = getIntent().getIntExtra("USER_ID", -1);

        if (view.getId() == R.id.btn_back_view_user) finish();
        else if (view.getId() == R.id.textInputRoleView) textInputRoleView.showDropDown();
        else if (view.getId() == R.id.btn_delete_user_view)
            new AlertDialog.Builder(AdminViewUserActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin menghapus data ini?")
                    .setPositiveButton("YES", (dialog, which) -> deleteUser(userId))
                    .setNegativeButton("NO", null).show();
        else if (view.getId() == R.id.btn_update_user_view) {
            populateUserDropdown();

            enableFields();

            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        }
        else if (view.getId() == R.id.btn_save_update_user) {
            Address address = new Address.Builder()
                    .setWhatsapp(Objects.requireNonNull(textInputWhatsappView.getText()).toString())
                    .setStreet(Objects.requireNonNull(textInputAddressView.getText()).toString())
                    .setSubDistrict(Objects.requireNonNull(textInputSubDistrictView.getText()).toString())
                    .setDistrict(Objects.requireNonNull(textInputDistrictView.getText()).toString())
                    .setCity(Objects.requireNonNull(textInputCityView.getText()).toString())
                    .setProvince(Objects.requireNonNull(textInputProvinceView.getText()).toString())
                    .setPostalCode(Objects.requireNonNull(textInputPostalCodeView.getText()).toString())
                    .build();

            User user = new User.Builder()
                    .setId(userId)
                    .setPassword(Objects.requireNonNull(textInputPasswordView.getText()).toString())
                    .setFullname(Objects.requireNonNull(textInputFullnameView.getText()).toString())
                    .setUsername(Objects.requireNonNull(textInputUsernameView.getText()).toString())
                    .setRole(textInputRoleView.getText().toString())
                    .setAddress(address)
                    .build();

            new AlertDialog.Builder(AdminViewUserActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin mengubah data ini?")
                    .setPositiveButton("YES", (dialog, which) -> updateUser(user))
                    .setNegativeButton("NO", null)
                    .show();
        }
    }

    public void updateUser(User user) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().updateUser(user.getId(), user);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) finish();
                    else {
                        new AlertDialog.Builder(AdminViewUserActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminViewUserActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewUserActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    public void deleteUser(int id) {
        Call<BaseResponse<Void>> call = UserAPIConfiguration.getInstance().deleteUser(id);
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Void>> call, @NonNull Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Void> baseResponse = response.body();
                    assert baseResponse != null;
                    if (baseResponse.isSuccess()) finish();
                    else {
                        new AlertDialog.Builder(AdminViewUserActivity.this)
                                .setTitle("GAGAL")
                                .setMessage(baseResponse.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminViewUserActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Void>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewUserActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void populateUserDropdown() {
        String[] roles = new String[]{"Admin", "Commissioner", "Director", "Shipper"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        textInputRoleView.setAdapter(adapter);

        textInputRoleView.setOnClickListener(v -> {
            if (adapter.getCount() > 0) textInputRoleView.showDropDown();
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
