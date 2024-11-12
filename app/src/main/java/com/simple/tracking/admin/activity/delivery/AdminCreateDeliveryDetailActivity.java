package com.simple.tracking.admin.activity.delivery;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;
import com.simple.tracking.network.ShipperAPIConfiguration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCreateDeliveryDetailActivity extends AppCompatActivity {

    private TextInputEditText textInputDeliveryNumberCreate;
    private TextInputEditText textInputCompanyNameCreate;
    private MaterialAutoCompleteTextView textInputStatusCreate;
    private MaterialAutoCompleteTextView textInputShipperCreate;
    private EditText textInputDeliveryDateCreate;
    private EditText textInputReceiveDateCreate;
    private ActivityResultLauncher<Intent> successActivityLauncher;

    private List<Shipper> shipperList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_delivery_detail);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        CardView btnGenerateDeliveryNumber = findViewById(R.id.btn_generate_create_delivery_detail);
        textInputDeliveryNumberCreate = findViewById(R.id.textInputDeliveryNumberCreateDeliveryDetail);
        textInputCompanyNameCreate = findViewById(R.id.textInputCompanyNameCreateDeliveryDetail);
        textInputStatusCreate = findViewById(R.id.textInputStatusCreateDeliveryDetail);
        textInputShipperCreate = findViewById(R.id.textInputShipperCreateDeliveryDetail);
        textInputDeliveryDateCreate = findViewById(R.id.textInputDeliveryDateCreateDeliveryDetail);
        textInputReceiveDateCreate = findViewById(R.id.textInputReceiveDateCreateDeliveryDetail);
        ImageView btnBack = findViewById(R.id.btn_back_delivery_detail_create);
        ImageView btnNext = findViewById(R.id.btn_next_delivery_recipient_create);

        String[] statuses = {"Diproses", "Dikirim", "Diterima"};

        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statuses);
        textInputStatusCreate.setAdapter(adapterRole);

        textInputStatusCreate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)textInputStatusCreate.showDropDown();
        });

        btnGenerateDeliveryNumber.setOnClickListener(v -> getDeliveryNumber());

        textInputStatusCreate.setOnClickListener(v -> textInputStatusCreate.showDropDown());

        getShippers();

        textInputShipperCreate.setOnClickListener(v -> textInputShipperCreate.showDropDown());

        textInputDeliveryDateCreate.setOnClickListener(v -> showDatePickerDialog(textInputDeliveryDateCreate));

        textInputReceiveDateCreate.setOnClickListener(v -> showDatePickerDialog(textInputReceiveDateCreate));

        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String shipperName = textInputShipperCreate.getText().toString();

            Shipper selectedShipper = null;
            if (shipperList != null && !shipperList.isEmpty()) {
                selectedShipper = shipperList
                        .stream()
                        .filter(shipper -> shipper.getUser().getFullname().equals(shipperName))
                        .findFirst()
                        .orElse(null);
            }

            Delivery delivery = new Delivery();
            delivery.setDeliveryNumber(Objects.requireNonNull(textInputDeliveryNumberCreate.getText()).toString());
            delivery.setCompanyName(Objects.requireNonNull(textInputCompanyNameCreate.getText()).toString());
            delivery.setStatus(textInputStatusCreate.getText().toString());

            if (selectedShipper != null) {
                delivery.setShipperId(selectedShipper.getId());
            }

            delivery.setDeliveryDate(null);
            delivery.setReceiveDate(null);


            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("id-ID"));

                String deliveryDateString = textInputDeliveryDateCreate.getText().toString();
                if (!deliveryDateString.isEmpty()) {
                    java.util.Date parsedDate = dateFormat.parse(deliveryDateString);
                    assert parsedDate != null;
                    Timestamp deliveryDateTimestamp = new Timestamp(parsedDate.getTime());
                    delivery.setDeliveryDate(deliveryDateTimestamp);
                }

                String receiveDateString = textInputReceiveDateCreate.getText().toString();
                if (!receiveDateString.isEmpty()) {
                    java.util.Date parsedDate = dateFormat.parse(receiveDateString);
                    assert parsedDate != null;
                    Timestamp receiveDateTimestamp = new Timestamp(parsedDate.getTime());
                    delivery.setReceiveDate(receiveDateTimestamp);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(AdminCreateDeliveryDetailActivity.this, AdminCreateDeliveryRecipientActivity.class);
            intent.putExtra("DELIVERY_DATA", delivery);
            successActivityLauncher.launch(intent);
        });

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                finish();
            }
        });
    }

    private void getDeliveryNumber() {
        Call<BaseResponse<String>> call = DeliveryAPIConfiguration.getInstance().getDeliveryNumber();
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<String>> call, @NonNull Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<String> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        String deliveryNumber = baseResponse.getData();
                        textInputDeliveryNumberCreate.setText(deliveryNumber);
                    } else {
                        new AlertDialog.Builder(AdminCreateDeliveryDetailActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminCreateDeliveryDetailActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<String>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminCreateDeliveryDetailActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void getShippers() {
        Call<BaseResponse<List<Shipper>>> call = ShipperAPIConfiguration.getInstance().getShippers(null, false, null, null);
        call.enqueue(new Callback<BaseResponse<List<Shipper>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<Shipper>>> call, @NonNull Response<BaseResponse<List<Shipper>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<Shipper>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        shipperList = baseResponse.getData();
                        populateShipperDropdown(shipperList);
                    } else {
                        new AlertDialog.Builder(AdminCreateDeliveryDetailActivity.this)
                                .setTitle("ERROR")
                                .setMessage("Terjadi kesalahan pada sistem kami.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminCreateDeliveryDetailActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<Shipper>>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminCreateDeliveryDetailActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void populateShipperDropdown(List<Shipper> shippers) {
        ArrayAdapter<Shipper> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, shippers);
        textInputShipperCreate.setAdapter(adapter);

        textInputShipperCreate.setOnClickListener(v -> {
            if (adapter.getCount() > 0) {
                textInputShipperCreate.showDropDown();
            }
        });
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format(Locale.forLanguageTag("id-ID"), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
    }
}
