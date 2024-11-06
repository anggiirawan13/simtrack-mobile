package com.simple.tracking.admin.activity.delivery;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.R;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryRecipient;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateDeliveryRecipientActivity extends AppCompatActivity implements View.OnClickListener {

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
    private ActivityResultLauncher<Intent> successActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_delivery_recipient);

        btnBackCreateDeliveryRecipientCreate = findViewById(R.id.btn_back_create_delivery_recipient);
        textInputFullnameCreate = findViewById(R.id.textInputFullnameCreateDeliveryRecipient);
        textInputWhatsappCreate = findViewById(R.id.textInputWhatsappCreateDeliveryRecipient);
        textInputAddressCreate = findViewById(R.id.textInputAddressCreateDeliveryRecipient);
        textInputSubDistrictCreate = findViewById(R.id.textInputSubDistrictCreateDeliveryRecipient);
        textInputDistrictCreate = findViewById(R.id.textInputDistrictCreateDeliveryRecipient);
        textInputCityCreate = findViewById(R.id.textInputCityCreateDeliveryRecipient);
        textInputProvinceCreate = findViewById(R.id.textInputProvinceCreateDeliveryRecipient);
        textInputPostalCodeCreate = findViewById(R.id.textInputPostalCodeCreateDeliveryRecipient);
        btnSaveCreateDeliveryRecipientCreate = findViewById(R.id.btn_save_create_delivery_recipient);

        loadInputData();

        btnBackCreateDeliveryRecipientCreate.setOnClickListener(v -> {
            saveInputData();
            finish();
        });

        btnSaveCreateDeliveryRecipientCreate.setOnClickListener(this);

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK);
                // Arahkan kembali ke DeliveryFragment
                finish(); // Menghentikan AdminCreateDeliveryRecipientActivity
            }
        });
    }

    private void saveInputData() {
        getSharedPreferences("delivery_prefs", MODE_PRIVATE)
                .edit()
                .putString("fullname", textInputFullnameCreate.getText().toString())
                .putString("whatsapp", textInputWhatsappCreate.getText().toString())
                .putString("address", textInputAddressCreate.getText().toString())
                .putString("subDistrict", textInputSubDistrictCreate.getText().toString())
                .putString("district", textInputDistrictCreate.getText().toString())
                .putString("city", textInputCityCreate.getText().toString())
                .putString("province", textInputProvinceCreate.getText().toString())
                .putString("postalCode", textInputPostalCodeCreate.getText().toString())
                .apply();
    }

    private void loadInputData() {
        SharedPreferences prefs = getSharedPreferences("delivery_prefs", MODE_PRIVATE);
        if (prefs != null && prefs.contains("fullname")) {
            textInputFullnameCreate.setText(prefs.getString("fullname", ""));
            textInputWhatsappCreate.setText(prefs.getString("whatsapp", ""));
            textInputAddressCreate.setText(prefs.getString("address", ""));
            textInputSubDistrictCreate.setText(prefs.getString("subDistrict", ""));
            textInputDistrictCreate.setText(prefs.getString("district", ""));
            textInputCityCreate.setText(prefs.getString("city", ""));
            textInputProvinceCreate.setText(prefs.getString("province", ""));
            textInputPostalCodeCreate.setText(prefs.getString("postalCode", ""));
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_create_delivery_recipient) {
            DeliveryRecipient recipient = new DeliveryRecipient();
            recipient.setName(textInputFullnameCreate.getText().toString());
            recipient.setStreet(textInputAddressCreate.getText().toString());
            recipient.setSubDistrict(textInputSubDistrictCreate.getText().toString());
            recipient.setDistrict(textInputDistrictCreate.getText().toString());
            recipient.setCity(textInputCityCreate.getText().toString());
            recipient.setProvince(textInputProvinceCreate.getText().toString());
            recipient.setPostalCode(textInputPostalCodeCreate.getText().toString());

            Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");
            delivery.setRecipient(recipient);

            Intent intent = new Intent(AdminCreateDeliveryRecipientActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "CREATE");
            intent.putExtra("MENU_NAME", "DELIVERY");
            intent.putExtra("DELIVERY_DATA", delivery);  // Mengirimkan objek Delivery
            successActivityLauncher.launch(intent);
        }
    }

    public void createDelivery(Delivery delivery) {
        Call<BaseResponse<Delivery>> call = DeliveryAPIConfiguration.getInstance().createDelivery(delivery); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<Delivery>>() {
            @Override
            public void onResponse(Call<BaseResponse<Delivery>> call, Response<BaseResponse<Delivery>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Delivery> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminCreateDeliveryRecipientActivity.this, "Delivery Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminCreateDeliveryRecipientActivity.this, "Delivery Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Delivery>> call, Throwable t) {
                Log.e("API Error", "Delivery Gagal Ditambahkan");
            }
        });
    }
}
