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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.R;
import com.simple.tracking.model.Delivery;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        String[] shippers = {"Shipper 1", "Shipper 2", "Shipper 3"};

        ArrayAdapter<String> adapterShipper = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, shippers);
        textInputShipperCreate.setAdapter(adapterShipper);

        textInputShipperCreate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textInputShipperCreate.showDropDown();
                }
            }
        });

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
                Delivery delivery = new Delivery();
                delivery.setDeliveryNumber(textInputDeliveryNumberCreate.getText().toString());
                delivery.setCompanyName(textInputCompanyNameCreate.getText().toString());
                delivery.setStatus(textInputStatusCreate.getText().toString());
                delivery.setShipperId(Integer.parseInt(textInputShipperCreate.getText().toString()));

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

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        editText.setText(date);
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }
}
