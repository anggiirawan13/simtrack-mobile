package com.simple.tracking.admin.activity.shipper;

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
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.ShipperAPIConfiguration;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateShipperActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialAutoCompleteTextView textInputUserIdCreate;
    private TextInputEditText textInputDeviceMappingCreate;

    private ActivityResultLauncher<Intent> successActivityLauncher;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_shipper);

        // Initialize views
        textInputUserIdCreate = findViewById(R.id.textInputUserIdCreate);
        textInputDeviceMappingCreate = findViewById(R.id.textInputDeviceMappingCreate);

        ImageView btnBack = findViewById(R.id.btn_back_create_shipper);
        btnBack.setOnClickListener(v -> finish());

        CardView btnCreate = findViewById(R.id.btn_save_create_shipper);
        btnCreate.setOnClickListener(this);

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                finish(); // Exit activity on success
            }
        });

        // Fetch users and populate dropdown
        getUsers();
    }

    public void createShipper(Shipper shipper) {
        Call<BaseResponse<Shipper>> call = ShipperAPIConfiguration.getInstance().createShipper(shipper); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<Shipper>>() {
            @Override
            public void onResponse(Call<BaseResponse<Shipper>> call, Response<BaseResponse<Shipper>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Shipper> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminCreateShipperActivity.this, "Shipper Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminCreateShipperActivity.this, "Shipper Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successfulGAGAL: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Shipper>> call, Throwable t) {
                Log.e("API Error", "Shipper Gagal Ditambahkan");
            }
        });
    }

    private void getUsers() {
        Call<BaseResponse<List<User>>> call = UserAPIConfiguration.getInstance().getUsers();
        call.enqueue(new Callback<BaseResponse<List<User>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<User>>> call, Response<BaseResponse<List<User>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<User>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        userList = baseResponse.getData();
                        populateUserDropdown(userList);
                    } else {
                        Toast.makeText(AdminCreateShipperActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminCreateShipperActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<User>>> call, Throwable t) {
                Toast.makeText(AdminCreateShipperActivity.this, "Failed to get users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUserDropdown(List<User> users) {
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, users);
        textInputUserIdCreate.setAdapter(adapter);

        textInputUserIdCreate.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected user directly from the parent
            User selectedUser = (User) parent.getItemAtPosition(position);
            // No need to setText here, just log for debugging
            Log.d("Selected User", "User selected: " + selectedUser.getFullname());
        });

        // Open the dropdown when the text field is clicked
        textInputUserIdCreate.setOnClickListener(v -> {
            if (adapter.getCount() > 0) {
                textInputUserIdCreate.showDropDown();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_create_shipper) {
            String userName = textInputUserIdCreate.getText().toString();

            // Find the selected user from the userList based on userName
            User selectedUser = null;
            for (User user : userList) {
                if (user.getFullname().equals(userName)) {
                    selectedUser = user;
                    break;
                }
            }

            // Check if selected user is found
            if (selectedUser != null) {
                Log.d("Selected User", selectedUser.toString());

                Shipper shipper = new Shipper();
                shipper.setUserId(selectedUser.getId());
                shipper.setDeviceMapping(textInputDeviceMappingCreate.getText().toString());

                Intent intent = new Intent(AdminCreateShipperActivity.this, ConfirmActivity.class);
                intent.putExtra("ACTION_TYPE", "CREATE");
                intent.putExtra("MENU_NAME", "SHIPPER");
                intent.putExtra("SHIPPER_DATA", shipper);
                successActivityLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
