package com.simple.tracking.admin.activity.delivery;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.R;

import java.util.Calendar;

public class AdminViewDeliveryDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_delivery_detail);

        String[] roles = {"Diproses", "Dikirim", "Diterima"};
        MaterialAutoCompleteTextView roleSpinner = findViewById(R.id.roleSpinner);
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        roleSpinner.setAdapter(adapterRole);

        roleSpinner.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                roleSpinner.showDropDown();
            }
        });

        roleSpinner.setOnClickListener(v -> roleSpinner.showDropDown());

        String[] shippers = {"Shipper 1", "Shipper 2", "Shipper 3"};
        MaterialAutoCompleteTextView shipperSpinner = findViewById(R.id.roleSpinner2);
        ArrayAdapter<String> adapterShipper = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, shippers);
        shipperSpinner.setAdapter(adapterShipper);

        shipperSpinner.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                shipperSpinner.showDropDown();
            }
        });

        shipperSpinner.setOnClickListener(v -> shipperSpinner.showDropDown());

        EditText editTextDate2 = findViewById(R.id.editTextDate2);
        editTextDate2.setOnClickListener(v -> showDatePickerDialog(editTextDate2));

        EditText editTextDate3 = findViewById(R.id.editTextDate3);
        editTextDate3.setOnClickListener(v -> showDatePickerDialog(editTextDate3));

        ImageView btnBack = findViewById(R.id.btn_back_delivery_detail_view);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnNext = findViewById(R.id.btn_next_delivery_recipient_view);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewDeliveryDetailActivity.this, AdminViewDeliveryRecipientActivity.class);
            startActivity(intent);
        });

        CardView btnDelete = findViewById(R.id.btn_delete_delivery_detail_view);
        btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewDeliveryDetailActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "DELETE");
            intent.putExtra("MENU_NAME", "DELIVERY");
            startActivity(intent);
        });

        CardView btnUpdate = findViewById(R.id.btn_update_delivery_detail_view);
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewDeliveryDetailActivity.this, AdminUpdateDeliveryDetailActivity.class);
            startActivity(intent);
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
                    String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }
}
