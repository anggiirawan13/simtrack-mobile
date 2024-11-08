package com.simple.tracking.admin.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.PreferenceManager;
import com.simple.tracking.R;
import com.simple.tracking.admin.adapter.UserAdapter;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText textInputFullnameView;
    private TextInputEditText textInputUsernameView;
    private TextInputEditText textInputPasswordView;
    private MaterialAutoCompleteTextView textInputRoleView;
    private TextInputEditText textInputAddressView;
    private TextInputEditText textInputSubDistrictView;
    private TextInputEditText textInputDistrictView;
    private TextInputEditText textInputCityView;
    private TextInputEditText textInputProvinceView;
    private TextInputEditText textInputPostalCodeView;
    private CardView btnDelete;
    private CardView btnUpdate;

    private ActivityResultLauncher<Intent> successActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user); // Use your actual layout name

        // Initialize views
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
        btnDelete = findViewById(R.id.btn_delete_user_view);
        btnUpdate = findViewById(R.id.btn_update_user_view);


        int id = getIntent().getIntExtra("USER_ID", 1);
        getUsers(id);

        PreferenceManager preferenceManager = new PreferenceManager(this);
        int userIdLogin = preferenceManager.getUserId();

        if (userIdLogin == id) {
            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        }

        String[] roles = new String[]{"Admin", "Commissioner", "Director", "Shipper"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        textInputRoleView.setAdapter(adapter);

        textInputRoleView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                textInputRoleView.showDropDown();
            }
        });

        textInputRoleView.setOnClickListener(view -> textInputRoleView.showDropDown());

        ImageView btnBack = findViewById(R.id.btn_back_view_user);
        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewUserActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "DELETE");
            intent.putExtra("MENU_NAME", "USER");
            intent.putExtra("USER_ID", getIntent().getIntExtra("USER_ID", 1));
            successActivityLauncher.launch(intent);
        });

        CardView btnSave = findViewById(R.id.btn_save_update_user);
        btnSave.setOnClickListener(this);

        btnUpdate.setOnClickListener(v -> {
            enableFields(true);

            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        });

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Arahkan kembali ke UserFragment
                setResult(RESULT_OK);
                finish(); // Menghentikan AdminCreateUserActivity
            }
        });
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
                        textInputFullnameView.setText(user.getFullname()); // Assuming getter is getFullname()
                        textInputUsernameView.setText(user.getUsername()); // Assuming getter is getUsername()
                        textInputPasswordView.setText(user.getPassword()); // Assuming getter is getPassword(), consider security implications
                        textInputRoleView.setText(user.getRole()); // Assuming getter is getRole()
                        textInputAddressView.setText(user.getAddress().getStreet()); // Assuming getter is getAddress()
                        textInputSubDistrictView.setText(user.getAddress().getSubDistrict()); // Assuming getter is getSubDistrict()
                        textInputDistrictView.setText(user.getAddress().getDistrict()); // Assuming getter is getDistrict()
                        textInputCityView.setText(user.getAddress().getCity()); // Assuming getter is getCity()
                        textInputProvinceView.setText(user.getAddress().getProvince()); // Assuming getter is getProvince()
                        textInputPostalCodeView.setText(user.getAddress().getPostalCode()); // Assuming getter is getPostalCode()
                    } else {
                        Toast.makeText(AdminViewUserActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminViewUserActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
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
        textInputFullnameView.setEnabled(enable);
        textInputUsernameView.setEnabled(enable);
        textInputPasswordView.setEnabled(enable);
        textInputRoleView.setEnabled(enable);
        textInputAddressView.setEnabled(enable);
        textInputSubDistrictView.setEnabled(enable);
        textInputDistrictView.setEnabled(enable);
        textInputCityView.setEnabled(enable);
        textInputProvinceView.setEnabled(enable);
        textInputPostalCodeView.setEnabled(enable);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_update_user) {
            Address address = new Address();
            address.setStreet(textInputAddressView.getText().toString());
            address.setSubDistrict(textInputSubDistrictView.getText().toString());
            address.setDistrict(textInputDistrictView.getText().toString());
            address.setCity(textInputCityView.getText().toString());
            address.setProvince(textInputProvinceView.getText().toString());
            address.setPostalCode(textInputPostalCodeView.getText().toString());

            User user = new User();
            user.setId(getIntent().getIntExtra("USER_ID", 1));
            user.setPassword(textInputPasswordView.getText().toString());
            user.setFullname(textInputFullnameView.getText().toString());
            user.setUsername(textInputUsernameView.getText().toString());
            user.setRole(textInputRoleView.getText().toString());
            user.setAddress(address);

            Intent intent = new Intent(AdminViewUserActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "UPDATE");
            intent.putExtra("MENU_NAME", "USER");
            intent.putExtra("USER_DATA", user);  // Mengirimkan objek User
            successActivityLauncher.launch(intent);
        }
    }

    public void updateUser(int id, User user) {
        Call<BaseResponse<User>> call = UserAPIConfiguration.getInstance().updateUser(id, user); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminViewUserActivity.this, "User Gagal Diubah", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminViewUserActivity.this, "User Gagal Diubah", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successfulGAGAL: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Log.e("API Error", "User Gagal Diubah");
            }
        });
    }

    public void deleteUser(int id) {
        Call<BaseResponse<Void>> call = UserAPIConfiguration.getInstance().deleteUser(id); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Void> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminViewUserActivity.this, "User Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminViewUserActivity.this, "User Gagal Dihapus", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successfulGAGAL: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e("API Error", "User Gagal Dihapus");
            }
        });
    }
}
