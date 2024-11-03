package com.simple.tracking.admin.activity.delivery;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.user.AdminCreateUserActivity;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.UserAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateDeliveryRecipientActivity extends AppCompatActivity {

    private ImageView btnBackCreateDeliveryRecipientCreate;
    private TextInputEditText textInputFullnameCreate;
    private TextInputEditText textInputWhatsappCreate;
    private TextInputEditText textInputAddressCreate;
    private TextInputEditText textInputSubDistrictCreate;
    private TextInputEditText textInputDistrictCreate;
    private TextInputEditText textInputCityCreate;
    private TextInputEditText textInputProvinceCreate;
    private TextInputEditText textInputPostalCodeCreate;
    private CardView btnSaveCreateDeliveryRecipientCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_delivery_recipient);

        btnBackCreateDeliveryRecipient = findViewById(R.id.btn_back_create_delivery_recipient);
        textInputFullname = findViewById(R.id.textInputFullnameCreateDeliveryRecipient);
        textInputWhatsapp = findViewById(R.id.textInputWhatsappCreateDeliveryRecipient);
        textInputAddress = findViewById(R.id.textInputAddressCreateDeliveryRecipient);
        textInputSubDistrict = findViewById(R.id.textInputSubDistrictCreateDeliveryRecipient);
        textInputDistrict = findViewById(R.id.textInputDistrictCreateDeliveryRecipient);
        textInputCity = findViewById(R.id.textInputCityCreateDeliveryRecipient);
        textInputProvince = findViewById(R.id.textInputProvinceCreateDeliveryRecipient);
        textInputPostalCode = findViewById(R.id.textInputPostalCodeCreateDeliveryRecipient);
        btnSaveCreateDeliveryRecipient = findViewById(R.id.btn_save_create_delivery_recipient);

        btnBackCreateDeliveryRecipient.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCreateDeliveryRecipientActivity.this, AdminCreateDeliveryDetailActivity.class);
            startActivity(intent);
        });

        btnSaveCreateDeliveryRecipient.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCreateDeliveryRecipientActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "CREATE");
            intent.putExtra("MENU_NAME", "DELIVERY");
            startActivity(intent);
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
