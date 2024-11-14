package com.simple.tracking.admin.activity.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.delivery.AdminViewDeliveryRecipientActivity;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryRecipient;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewDeliveryRecipientActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView btnSaveUpdateDeliveryRecipientUpdate, btnDeleteDeliveryRecipientUpdate, btnUpdateDeliveryRecipientUpdate;
    private TextInputEditText textInputFullnameUpdate, textInputWhatsappUpdate,
            textInputAddressUpdate, textInputSubDistrictUpdate, textInputDistrictUpdate,
            textInputCityUpdate, textInputProvinceUpdate, textInputPostalCodeUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_delivery_recipient);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        textInputFullnameUpdate = findViewById(R.id.textInputFullnameUpdateDeliveryRecipient);
        textInputWhatsappUpdate = findViewById(R.id.textInputWhatsappUpdateDeliveryRecipient);
        textInputAddressUpdate = findViewById(R.id.textInputAddressUpdateDeliveryRecipient);
        textInputSubDistrictUpdate = findViewById(R.id.textInputSubDistrictUpdateDeliveryRecipient);
        textInputDistrictUpdate = findViewById(R.id.textInputDistrictUpdateDeliveryRecipient);
        textInputCityUpdate = findViewById(R.id.textInputCityUpdateDeliveryRecipient);
        textInputProvinceUpdate = findViewById(R.id.textInputProvinceUpdateDeliveryRecipient);
        textInputPostalCodeUpdate = findViewById(R.id.textInputPostalCodeUpdateDeliveryRecipient);
        btnSaveUpdateDeliveryRecipientUpdate = findViewById(R.id.btn_save_update_delivery_recipient);
        btnUpdateDeliveryRecipientUpdate = findViewById(R.id.btn_update_delivery_recipient_update);
        btnDeleteDeliveryRecipientUpdate = findViewById(R.id.btn_delete_delivery_recipient_update);

        ImageView btnBackUpdateDeliveryRecipientUpdate = findViewById(R.id.btn_back_delivery_recipient_update);
        btnBackUpdateDeliveryRecipientUpdate.setOnClickListener(this);

        btnUpdateDeliveryRecipientUpdate.setOnClickListener(this);

        btnSaveUpdateDeliveryRecipientUpdate.setOnClickListener(this);

        btnDeleteDeliveryRecipientUpdate.setOnClickListener(this);

        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) enableFields();

        loadInputData();

        saveInputData();
    }

    public void setValueFields(Delivery delivery) {
        textInputFullnameUpdate.setText(delivery.getRecipient().getName());
        textInputWhatsappUpdate.setText(delivery.getRecipient().getAddress().getWhatsapp());
        textInputAddressUpdate.setText(delivery.getRecipient().getAddress().getStreet());
        textInputSubDistrictUpdate.setText(delivery.getRecipient().getAddress().getSubDistrict());
        textInputDistrictUpdate.setText(delivery.getRecipient().getAddress().getDistrict());
        textInputCityUpdate.setText(delivery.getRecipient().getAddress().getCity());
        textInputProvinceUpdate.setText(delivery.getRecipient().getAddress().getProvince());
        textInputPostalCodeUpdate.setText(delivery.getRecipient().getAddress().getPostalCode());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_delivery_recipient_update) {
            saveInputData();

            boolean isEdit = btnUpdateDeliveryRecipientUpdate.getVisibility() == View.GONE;

            Intent intent = new Intent();
            intent.putExtra("isEdit", isEdit);

            setResult(RESULT_CANCELED, intent);

            finish();
        } else if (view.getId() == R.id.btn_save_update_delivery_recipient)
            new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin mengubah data ini?")
                    .setPositiveButton("YES", (dialog, which) -> updateDelivery())
                    .setNegativeButton("NO", null)
                    .show();
        else if (view.getId() == R.id.btn_delete_delivery_recipient_update) {
            int id = getIntent().getIntExtra("DELIVERY_ID", -1);

            new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin menghapus data ini?")
                    .setPositiveButton("YES", (dialog, which) -> deleteDelivery(id))
                    .setNegativeButton("NO", null)
                    .show();
        } else if (view.getId() == R.id.btn_update_delivery_recipient_update) {
            enableFields();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
    }

    private void enableFields() {
        textInputFullnameUpdate.setEnabled(true);
        textInputWhatsappUpdate.setEnabled(true);
        textInputAddressUpdate.setEnabled(true);
        textInputSubDistrictUpdate.setEnabled(true);
        textInputDistrictUpdate.setEnabled(true);
        textInputCityUpdate.setEnabled(true);
        textInputProvinceUpdate.setEnabled(true);
        textInputPostalCodeUpdate.setEnabled(true);
        btnSaveUpdateDeliveryRecipientUpdate.setEnabled(true);
        btnUpdateDeliveryRecipientUpdate.setEnabled(true);
        btnDeleteDeliveryRecipientUpdate.setEnabled(true);

        btnSaveUpdateDeliveryRecipientUpdate.setVisibility(View.VISIBLE);
        btnUpdateDeliveryRecipientUpdate.setVisibility(View.GONE);
        btnDeleteDeliveryRecipientUpdate.setVisibility(View.GONE);
    }

    private void saveInputData() {
        boolean isEdit = btnUpdateDeliveryRecipientUpdate.getVisibility() == View.GONE;
        getSharedPreferences("delivery_update_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isEdit", isEdit)
                .putString("fullname", Objects.requireNonNull(textInputFullnameUpdate.getText()).toString())
                .putString("whatsapp", Objects.requireNonNull(textInputWhatsappUpdate.getText()).toString())
                .putString("address", Objects.requireNonNull(textInputAddressUpdate.getText()).toString())
                .putString("subDistrict", Objects.requireNonNull(textInputSubDistrictUpdate.getText()).toString())
                .putString("district", Objects.requireNonNull(textInputDistrictUpdate.getText()).toString())
                .putString("city", Objects.requireNonNull(textInputCityUpdate.getText()).toString())
                .putString("province", Objects.requireNonNull(textInputProvinceUpdate.getText()).toString())
                .putString("postalCode", Objects.requireNonNull(textInputPostalCodeUpdate.getText()).toString())
                .apply();
    }

    private void loadInputData() {
        SharedPreferences prefs = getSharedPreferences("delivery_update_prefs", MODE_PRIVATE);
        if (prefs != null && prefs.contains("fullname")) {
            boolean isEdit = prefs.getBoolean("isEdit", false);
            if (isEdit) enableFields();

            textInputFullnameUpdate.setText(prefs.getString("fullname", ""));
            textInputWhatsappUpdate.setText(prefs.getString("whatsapp", ""));
            textInputAddressUpdate.setText(prefs.getString("address", ""));
            textInputSubDistrictUpdate.setText(prefs.getString("subDistrict", ""));
            textInputDistrictUpdate.setText(prefs.getString("district", ""));
            textInputCityUpdate.setText(prefs.getString("city", ""));
            textInputProvinceUpdate.setText(prefs.getString("province", ""));
            textInputPostalCodeUpdate.setText(prefs.getString("postalCode", ""));
        } else {
            Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");
            setValueFields(delivery);
        }
    }

    public void updateDelivery() {
        Delivery delivery = prepareDataUpdate();

        Call<BaseResponse<Delivery>> call = DeliveryAPIConfiguration.getInstance().updateDelivery(delivery.getId(), delivery);
        call.enqueue(new Callback<BaseResponse<Delivery>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Delivery>> call, @NonNull Response<BaseResponse<Delivery>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Delivery> baseResponse = response.body();
                    assert baseResponse != null;
                    if (baseResponse.isSuccess()) {
                        getSharedPreferences("delivery_update_prefs", MODE_PRIVATE).edit().clear().apply();
                        getSharedPreferences("delivery_prefs", MODE_PRIVATE).edit().clear().apply();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                                .setTitle("GAGAL")
                                .setMessage(baseResponse.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Delivery>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private Delivery prepareDataUpdate() {
        Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");

        Address address = new Address.Builder()
                .setId(delivery.getRecipient().getAddress().getId())
                .setWhatsapp(Objects.requireNonNull(textInputWhatsappUpdate.getText()).toString())
                .setStreet(Objects.requireNonNull(textInputAddressUpdate.getText()).toString())
                .setSubDistrict(Objects.requireNonNull(textInputSubDistrictUpdate.getText()).toString())
                .setDistrict(Objects.requireNonNull(textInputDistrictUpdate.getText()).toString())
                .setCity(Objects.requireNonNull(textInputCityUpdate.getText()).toString())
                .setProvince(Objects.requireNonNull(textInputProvinceUpdate.getText()).toString())
                .setPostalCode(Objects.requireNonNull(textInputPostalCodeUpdate.getText()).toString())
                .build();

        DeliveryRecipient recipient = new DeliveryRecipient.Builder()
                .setId(delivery.getRecipient().getId())
                .setName(Objects.requireNonNull(textInputFullnameUpdate.getText()).toString())
                .setAddress(address)
                .build();

        delivery.setRecipient(recipient);

        return delivery;
    }

    public void deleteDelivery(int id) {
        Call<BaseResponse<Void>> call = DeliveryAPIConfiguration.getInstance().deleteDelivery(id);
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Void>> call, @NonNull Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Void> baseResponse = response.body();
                    assert baseResponse != null;
                    if (baseResponse.isSuccess()) finish();
                    else {
                        new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                                .setTitle("GAGAL")
                                .setMessage(baseResponse.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Void>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}
