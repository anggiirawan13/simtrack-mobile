package com.simple.tracking.admin.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.R;
import com.simple.tracking.SuccessActivity;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText textInputFullnameCreate;
    private TextInputEditText textInputUsernameCreate;

    private TextInputEditText textInputPasswordCreate;

    private MaterialAutoCompleteTextView textInputRoleCreate;
    private TextInputEditText textInputAddressCreate;
    private TextInputEditText textInputSubDistrictCreate;
    private TextInputEditText textInputDistrictCreate;
    private TextInputEditText textInputCityCreate;
    private TextInputEditText textInputProvinceCreate;
    private TextInputEditText textInputPostalCodeCreate;

    private ActivityResultLauncher<Intent> successActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_user); // Use your actual layout name

        // Initialize views
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
            if (hasFocus) {
                textInputRoleCreate.showDropDown();
            }
        });

        textInputRoleCreate.setOnClickListener(view -> textInputRoleCreate.showDropDown());

        ImageView btnBack = findViewById(R.id.btn_back_create_user);
        btnBack.setOnClickListener(v -> finish());

        CardView btnCreate = findViewById(R.id.btn_save_create_user);
        btnCreate.setOnClickListener(this);

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Arahkan kembali ke UserFragment
                finish(); // Menghentikan AdminCreateUserActivity
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_create_user) {
            Address address = new Address();
            address.setStreet(textInputAddressCreate.getText().toString());
            address.setSubDistrict(textInputSubDistrictCreate.getText().toString());
            address.setDistrict(textInputDistrictCreate.getText().toString());
            address.setCity(textInputCityCreate.getText().toString());
            address.setProvince(textInputProvinceCreate.getText().toString());
            address.setPostalCode(textInputPostalCodeCreate.getText().toString());

            User user = new User();
            user.setPassword(textInputPasswordCreate.getText().toString());
            user.setFullname(textInputFullnameCreate.getText().toString());
            user.setUsername(textInputUsernameCreate.getText().toString());
            user.setRole(textInputRoleCreate.getText().toString());
            user.setAddress(address);

            Intent intent = new Intent(AdminCreateUserActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "CREATE");
            intent.putExtra("MENU_NAME", "USER");
            intent.putExtra("USER_DATA", user);  // Mengirimkan objek User
            successActivityLauncher.launch(intent);
        }
    }

    public void createUser(User user) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().createUser(user); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminCreateUserActivity.this, "User Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminCreateUserActivity.this, "User Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Log.e("API Error", "User Gagal Ditambahkan");
            }
        });
    }
}
