package com.simple.tracking.admin.activity.delivery;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.R;
import com.simple.tracking.admin.activity.shipper.AdminCreateShipperActivity;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.ShipperAPIConfiguration;
import com.simple.tracking.network.UserAPIConfiguration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
    private ImageView btnBack;
    private ImageView btnNext;
    private ActivityResultLauncher<Intent> successActivityLauncher;

    private List<Shipper> shipperList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_delivery_detail);

        textInputDeliveryNumberCreate = findViewById(R.id.textInputDeliveryNumberCreateDeliveryDetail);
        textInputCompanyNameCreate = findViewById(R.id.textInputCompanyNameCreateDeliveryDetail);
        textInputStatusCreate = findViewById(R.id.textInputStatusCreateDeliveryDetail);
        textInputShipperCreate = findViewById(R.id.textInputShipperCreateDeliveryDetail);
        textInputDeliveryDateCreate = findViewById(R.id.textInputDeliveryDateCreateDeliveryDetail);
        textInputReceiveDateCreate = findViewById(R.id.textInputReceiveDateCreateDeliveryDetail);
        btnBack = findViewById(R.id.btn_back_delivery_detail_create);
        btnNext = findViewById(R.id.btn_next_delivery_recipient_create);

        String[] statuses = {"Diproses", "Dikirim", "Diterima"};

        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statuses);
        textInputStatusCreate.setAdapter(adapterRole);

        textInputStatusCreate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputStatusCreate.showDropDown();
                }
            }
        });

        textInputStatusCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputStatusCreate.showDropDown();
            }
        });

        getShippers();

        textInputShipperCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputShipperCreate.showDropDown();
            }
        });

        textInputDeliveryDateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textInputDeliveryDateCreate);
            }
        });

        textInputReceiveDateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(textInputReceiveDateCreate);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shipperName = textInputShipperCreate.getText().toString();

                Shipper selectedShipper = shipperList
                        .stream()
                        .filter(shipper -> shipper.getUser().getFullname().equals(shipperName))
                        .findFirst()
                        .orElse(null);

                Delivery delivery = new Delivery();
                delivery.setDeliveryNumber(textInputDeliveryNumberCreate.getText().toString());
                delivery.setCompanyName(textInputCompanyNameCreate.getText().toString());
                delivery.setStatus(textInputStatusCreate.getText().toString());

                if (selectedShipper != null) {
                    delivery.setShipperId(selectedShipper.getId());
                }

                delivery.setDeliveryDate(null);
                delivery.setReceiveDate(null);


                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String deliveryDateString = textInputDeliveryDateCreate.getText().toString();
                    if (!deliveryDateString.isEmpty()) {
                        java.util.Date parsedDate = dateFormat.parse(deliveryDateString);
                        Timestamp deliveryDateTimestamp = new Timestamp(parsedDate.getTime());
                        delivery.setDeliveryDate(deliveryDateTimestamp);
                    }

                    String receiveDateString = textInputReceiveDateCreate.getText().toString();
                    if (!receiveDateString.isEmpty()) {
                        java.util.Date parsedDate = dateFormat.parse(receiveDateString);
                        Timestamp receiveDateTimestamp = new Timestamp(parsedDate.getTime());
                        delivery.setReceiveDate(receiveDateTimestamp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(AdminCreateDeliveryDetailActivity.this, AdminCreateDeliveryRecipientActivity.class);
                intent.putExtra("DELIVERY_DATA", delivery);
                successActivityLauncher.launch(intent);
            }
        });

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Arahkan kembali ke UserFragment
                finish(); // Menghentikan AdminCreateUserActivity
            }
        });
    }

    private void getShippers() {
        Call<BaseResponse<List<Shipper>>> call = ShipperAPIConfiguration.getInstance().getShippers();
        call.enqueue(new Callback<BaseResponse<List<Shipper>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Shipper>>> call, Response<BaseResponse<List<Shipper>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<Shipper>> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        shipperList = baseResponse.getData();
                        populateShipperDropdown(shipperList);
                    } else {
                        Toast.makeText(AdminCreateDeliveryDetailActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminCreateDeliveryDetailActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Shipper>>> call, Throwable t) {
                Toast.makeText(AdminCreateDeliveryDetailActivity.this, "Failed to get users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateShipperDropdown(List<Shipper> shippers) {
        ArrayAdapter<Shipper> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, shippers);
        textInputShipperCreate.setAdapter(adapter);

        // Open the dropdown when the text field is clicked
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
                    // Set the date in yyyy-MM-dd format
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

}
