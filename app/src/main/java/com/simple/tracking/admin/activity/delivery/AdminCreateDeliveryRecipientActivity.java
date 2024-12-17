package com.simple.tracking.admin.activity.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.model.Address;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryRecipient;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateDeliveryRecipientActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText textInputFullnameCreate, textInputWhatsappCreate,
            textInputAddressCreate, textInputSubDistrictCreate, textInputDistrictCreate,
            textInputCityCreate, textInputProvinceCreate, textInputPostalCodeCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_delivery_recipient);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        textInputFullnameCreate = findViewById(R.id.textInputFullnameCreateDeliveryRecipient);
        textInputWhatsappCreate = findViewById(R.id.textInputWhatsappCreateDeliveryRecipient);
        textInputAddressCreate = findViewById(R.id.textInputAddressCreateDeliveryRecipient);
        textInputSubDistrictCreate = findViewById(R.id.textInputSubDistrictCreateDeliveryRecipient);
        textInputDistrictCreate = findViewById(R.id.textInputDistrictCreateDeliveryRecipient);
        textInputCityCreate = findViewById(R.id.textInputCityCreateDeliveryRecipient);
        textInputProvinceCreate = findViewById(R.id.textInputProvinceCreateDeliveryRecipient);
        textInputPostalCodeCreate = findViewById(R.id.textInputPostalCodeCreateDeliveryRecipient);

        ImageView btnBackCreateDeliveryRecipientCreate = findViewById(R.id.btn_back_create_delivery_recipient);
        btnBackCreateDeliveryRecipientCreate.setOnClickListener(v -> {
            saveInputData();
            finish();
        });

        CardView btnSaveCreateDeliveryRecipientCreate = findViewById(R.id.btn_save_create_delivery_recipient);
        btnSaveCreateDeliveryRecipientCreate.setOnClickListener(this);

        loadInputData();
    }

    private void saveInputData() {
        getSharedPreferences("delivery_prefs", MODE_PRIVATE)
                .edit()
                .putString("fullname", Objects.requireNonNull(textInputFullnameCreate.getText()).toString())
                .putString("whatsapp", Objects.requireNonNull(textInputWhatsappCreate.getText()).toString())
                .putString("address", Objects.requireNonNull(textInputAddressCreate.getText()).toString())
                .putString("subDistrict", Objects.requireNonNull(textInputSubDistrictCreate.getText()).toString())
                .putString("district", Objects.requireNonNull(textInputDistrictCreate.getText()).toString())
                .putString("city", Objects.requireNonNull(textInputCityCreate.getText()).toString())
                .putString("province", Objects.requireNonNull(textInputProvinceCreate.getText()).toString())
                .putString("postalCode", Objects.requireNonNull(textInputPostalCodeCreate.getText()).toString())
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
            Address address = new Address.Builder()
                    .setWhatsapp(Objects.requireNonNull(textInputWhatsappCreate.getText()).toString())
                    .setStreet(Objects.requireNonNull(textInputAddressCreate.getText()).toString())
                    .setSubDistrict(Objects.requireNonNull(textInputSubDistrictCreate.getText()).toString())
                    .setDistrict(Objects.requireNonNull(textInputDistrictCreate.getText()).toString())
                    .setCity(Objects.requireNonNull(textInputCityCreate.getText()).toString())
                    .setProvince(Objects.requireNonNull(textInputProvinceCreate.getText()).toString())
                    .setPostalCode(Objects.requireNonNull(textInputPostalCodeCreate.getText()).toString())
                    .build();

            DeliveryRecipient recipient = new DeliveryRecipient.Builder()
                    .setName(Objects.requireNonNull(textInputFullnameCreate.getText()).toString())
                    .setAddress(address)
                    .build();

            Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");
            delivery.setRecipient(recipient);

            new AlertDialog.Builder(AdminCreateDeliveryRecipientActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin membuat data ini?")
                    .setPositiveButton("YES", (dialog, which) -> createDelivery(delivery))
                    .setNegativeButton("NO", null)
                    .show();
        }
    }

    public void createDelivery(Delivery delivery) {
        Call<BaseResponse<Delivery>> call = DeliveryAPIConfiguration.getInstance().createDelivery(delivery);
        call.enqueue(new Callback<BaseResponse<Delivery>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Delivery>> call, @NonNull Response<BaseResponse<Delivery>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Delivery> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        Delivery deliveryCreated = baseResponse.getData();
                        openWhatsApp(
                                deliveryCreated.getDeliveryNumber(),
                                deliveryCreated.getCompanyName(),
                                deliveryCreated.getDeliveryDate(),
                                delivery.getRecipient().getAddress().getWhatsapp(),
                                deliveryCreated.getStatus().getStatus(),
                                deliveryCreated.getConfirmationCode()
                        );
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        new AlertDialog.Builder(AdminCreateDeliveryRecipientActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminCreateDeliveryRecipientActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Delivery>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminCreateDeliveryRecipientActivity.this)
                        .setTitle("ERROR")
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
    private void openWhatsApp(String deliveryNumber, String companyName, Date deliveryDate, String whatsappNumber, String status, String confirmationCode) {
        if (whatsappNumber.startsWith("0")) whatsappNumber.substring(0).replace("0", "62");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedShippingDate = dateFormat.format(deliveryDate);

        String message = "Halo, *" + companyName + "*!\nPesanan Anda sudah dibuat dengan nomor resi: *" + deliveryNumber + "*.\n"
                + "Status pesanan saat ini: *" + status + "*.\n"
                + "Pesanan akan dikirimkan pada tanggal: *" + formattedShippingDate + "*.\n"
                + "Gunakan kode konfirmasi berikut untuk mengonfirmasi saat menerima pesanan: *" + confirmationCode + "*.";


        String url = "https://wa.me/" + whatsappNumber + "?text=" + Uri.encode(message);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
    }

}
