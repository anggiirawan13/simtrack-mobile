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

public class AdminCreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText textInputFullnameCreate, textInputUsernameCreate,
            textInputPasswordCreate, textInputAddressCreate, textInputSubDistrictCreate,
            textInputDistrictCreate, textInputCityCreate, textInputProvinceCreate,
            textInputPostalCodeCreate;
    private MaterialAutoCompleteTextView textInputRoleCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_user);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        textInputFullnameCreate = findViewById(R.id.textInputFullnameCreate);
        textInputUsernameCreate = findViewById(R.id.textInputUsernameCreate);
        textInputPasswordCreate = findViewById(R.id.textInputPasswordCreate);
        textInputRoleCreate = findViewById(R.id.textInputRoleCreate);
        textInputAddressCreate = findViewById(R.id.textInputAddressCreate);
        textInputSubDistrictCreate = findViewById(R.id.textInputSubDistrictCreate);
        textInputDistrictCreate = findViewById(R.id.textInputDistrictCreate);
        textInputCityCreate = findViewById(R.id.textInputCityCreate);
        textInputProvinceCreate = findViewById(R.id.textInputProvinceCreate);
        textInputPostalCodeCreate = findViewById(R.id.textInputPostalCodeCreate);

        String[] roles = new String[]{"Admin", "Commissioner", "Director", "Shipper"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        textInputRoleCreate.setAdapter(adapter);

        textInputRoleCreate.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) textInputRoleCreate.showDropDown();
        });

        textInputRoleCreate.setOnClickListener(this);

        ImageView btnBack = findViewById(R.id.btn_back_create_user);
        btnBack.setOnClickListener(this);

        CardView btnCreate = findViewById(R.id.btn_save_create_user);
        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_create_user) finish();
        if (view.getId() == R.id.textInputRoleCreate) textInputRoleCreate.showDropDown();
        else if (view.getId() == R.id.btn_save_create_user) {
            Address address = new Address.Builder()
                    .setStreet(Objects.requireNonNull(textInputAddressCreate.getText()).toString())
                    .setSubDistrict(Objects.requireNonNull(textInputSubDistrictCreate.getText()).toString())
                    .setDistrict(Objects.requireNonNull(textInputDistrictCreate.getText()).toString())
                    .setCity(Objects.requireNonNull(textInputCityCreate.getText()).toString())
                    .setProvince(Objects.requireNonNull(textInputProvinceCreate.getText()).toString())
                    .setPostalCode(Objects.requireNonNull(textInputPostalCodeCreate.getText()).toString())
                    .build();

            User user = new User.Builder()
                    .setPassword(Objects.requireNonNull(textInputPasswordCreate.getText()).toString())
                    .setFullname(Objects.requireNonNull(textInputFullnameCreate.getText()).toString())
                    .setUsername(Objects.requireNonNull(textInputUsernameCreate.getText()).toString())
                    .setRole(textInputRoleCreate.getText().toString()).setAddress(address)
                    .build();

            new AlertDialog.Builder(AdminCreateUserActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin membuat data ini?")
                    .setPositiveButton("YES", (dialog, which) -> createUser(user))
                    .setNegativeButton("NO", null)
                    .show();
        }
    }

    public void createUser(User user) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().createUser(user);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (!response.isSuccessful())
                    new AlertDialog.Builder(AdminCreateUserActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                else {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) finish();
                    else {
                        assert baseResponse != null;
                        new AlertDialog.Builder(AdminCreateUserActivity.this)
                                .setTitle("ERROR")
                                .setMessage(baseResponse.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminCreateUserActivity.this).
                        setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
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
