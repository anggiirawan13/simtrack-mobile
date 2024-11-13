package com.simple.tracking.admin.activity.delivery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryRecipient;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.DeliveryAPIConfiguration;
import com.simple.tracking.network.ShipperAPIConfiguration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewDeliveryDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter<Shipper> adapterShipper;
    private TextInputEditText deliveryNumberInput, companyNameInput, confirmationCodeInput;
    private MaterialAutoCompleteTextView statusSpinner, shipperSpinner;
    private EditText deliveryDateInput, receivingDateInput;
    private CardView btnDelete, btnUpdate;
    private List<Shipper> shipperList;

    private Delivery delivery;
    private ActivityResultLauncher<Intent> successActivityLauncher;


    public static String convertTimestampToString(Timestamp timestamp) {
        if (timestamp == null) return "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.forLanguageTag("id-ID"));

        return dateFormat.format(timestamp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_delivery_detail);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        ImageView btnBack = findViewById(R.id.btn_back_delivery_detail_view);
        ImageView btnNext = findViewById(R.id.btn_next_delivery_recipient_view);
        deliveryNumberInput = findViewById(R.id.deliery_number_admin_view_delivery_detail);
        companyNameInput = findViewById(R.id.company_name_admin_view_delivery_detail);
        statusSpinner = findViewById(R.id.status_admin_view_delivery_detail);
        shipperSpinner = findViewById(R.id.shipper_admin_view_delivery_detail);
        deliveryDateInput = findViewById(R.id.delivery_date_admin_view_delivery_detail);
        receivingDateInput = findViewById(R.id.receiving_date_admin_view_delivery_detail);
        confirmationCodeInput = findViewById(R.id.confirmation_code_admin_view_delivery_detail);
        btnDelete = findViewById(R.id.btn_delete_delivery_detail_view);
        btnUpdate = findViewById(R.id.btn_update_delivery_detail_view);

        getShippers();

        String[] statuses = {"Diproses", "Dikirim", "Diterima"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statuses);
        statusSpinner.setAdapter(adapterRole);

        statusSpinner.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) statusSpinner.showDropDown();
        });

        statusSpinner.setOnClickListener(v -> statusSpinner.showDropDown());

        deliveryDateInput.setOnClickListener(v -> showDatePickerDialog(deliveryDateInput));

        receivingDateInput.setOnClickListener(v -> showDatePickerDialog(receivingDateInput));

        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewDeliveryDetailActivity.this, AdminViewDeliveryRecipientActivity.class);
            intent.putExtra("DELIVERY_DATA", delivery);
            successActivityLauncher.launch(intent);
        });

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                finish();
            }
        });

        btnDelete.setOnClickListener(this);

        btnUpdate.setOnClickListener(this);
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


                        int id = getIntent().getIntExtra("DELIVERY_ID", 1);
                        getDeliveries(id);
                    } else {
                        Toast.makeText(AdminViewDeliveryDetailActivity.this, "Failed to load shippers", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminViewDeliveryDetailActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<Shipper>>> call, @NonNull Throwable t) {
                Toast.makeText(AdminViewDeliveryDetailActivity.this, "Failed to get shippers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateShipperDropdown(List<Shipper> shippers) {
        adapterShipper = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, shippers);
        shipperSpinner.setAdapter(adapterShipper);


        shipperSpinner.setOnClickListener(v -> {
            if (adapterShipper.getCount() > 0) {
                shipperSpinner.showDropDown();
            }
        });
    }

    public void getDeliveries(int id) {
        @NonNull Call<BaseResponse<Delivery>> call = DeliveryAPIConfiguration.getInstance().getDelivery(id);
        call.enqueue(new Callback<BaseResponse<Delivery>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Delivery>> call, @NonNull Response<BaseResponse<Delivery>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Delivery> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        delivery = baseResponse.getData();

                        deliveryNumberInput.setText(delivery.getDeliveryNumber());
                        companyNameInput.setText(delivery.getCompanyName());
                        statusSpinner.setText(delivery.getStatus());

                        for (Shipper shipper : shipperList) {
                            if (shipper.getId() == delivery.getShipperId()) {
                                shipperSpinner.setText(shipper.toString(), false);
                                break;
                            }
                        }

                        if (delivery.getDeliveryDate() != null) {
                            deliveryDateInput.setText(convertTimestampToString(delivery.getDeliveryDate()));
                        } else {
                            deliveryDateInput.setText("");
                        }

                        if (delivery.getReceiveDate() != null) {
                            receivingDateInput.setText(convertTimestampToString(delivery.getReceiveDate()));
                        } else {
                            receivingDateInput.setText("");
                        }

                        confirmationCodeInput.setText(delivery.getConfirmationCode());

                        if (!delivery.getStatus().equalsIgnoreCase("diterima"))
                            btnDelete.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(AdminViewDeliveryDetailActivity.this, "Failed to load delivery data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminViewDeliveryDetailActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Delivery>> call, @NonNull Throwable t) {
                Log.e("API Error", "Failed to fetch deliveries: " + t.getMessage());
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
                    String date = String.format(Locale.forLanguageTag("id-ID"), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void enableFields() {
        companyNameInput.setEnabled(true);
        statusSpinner.setEnabled(true);
        shipperSpinner.setEnabled(true);
        deliveryDateInput.setEnabled(true);
        receivingDateInput.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_delete_delivery_detail_view) {
            new AlertDialog.Builder(AdminViewDeliveryDetailActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin menghapus data ini?")
                    .setPositiveButton("YES", (dialog, which) -> {})
                    .setNegativeButton("NO", null)
                    .show();
        } else if (view.getId() == R.id.btn_update_delivery_detail_view)
            enableFields();

            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
    }
}
