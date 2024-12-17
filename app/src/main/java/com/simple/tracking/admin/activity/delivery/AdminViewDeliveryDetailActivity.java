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
import com.simple.tracking.PreferenceManager;
import com.simple.tracking.R;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryRecipient;
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

        PreferenceManager preferenceManager = new PreferenceManager(this);
        if (preferenceManager.isAdmin()) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }

        getShippers();

        String[] statuses = {"DIPROSES", "DIKIRIM", "DITERIMA"};

        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statuses);
        statusSpinner.setAdapter(adapterRole);

        statusSpinner.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) statusSpinner.showDropDown();
        });

        statusSpinner.setOnClickListener(v -> statusSpinner.showDropDown());

        deliveryDateInput.setOnClickListener(v -> showDatePickerDialog(deliveryDateInput));

        receivingDateInput.setOnClickListener(v -> showDatePickerDialog(receivingDateInput));

        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String shipperName = shipperSpinner.getText().toString();

            Shipper selectedShipper = null;
            if (shipperList != null && !shipperList.isEmpty()) {
                selectedShipper = shipperList
                        .stream()
                        .filter(shipper -> shipper.getUser().getFullname().equals(shipperName))
                        .findFirst()
                        .orElse(null);
            }

            if (selectedShipper != null) {
                delivery.setShipperId(selectedShipper.getId());
            }

            delivery.setCompanyName(Objects.requireNonNull(companyNameInput.getText()).toString());

            int idx = 1;
            for (String status : statuses) {
                if (status.equalsIgnoreCase(statusSpinner.getText().toString())) {
                    idx++;
                    break;
                }

                idx++;
            }
            delivery.setStatusId(idx);

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.forLanguageTag("id-ID"));

                String deliveryDateString = deliveryDateInput.getText().toString();
                if (!deliveryDateString.isEmpty()) {
                    java.util.Date parsedDate = dateFormat.parse(deliveryDateString);
                    assert parsedDate != null;
                    Timestamp deliveryDateTimestamp = new Timestamp(parsedDate.getTime());
                    delivery.setDeliveryDate(deliveryDateTimestamp);
                }

                String receiveDateString = receivingDateInput.getText().toString();
                if (!receiveDateString.isEmpty()) {
                    java.util.Date parsedDate = dateFormat.parse(receiveDateString);
                    assert parsedDate != null;
                    Timestamp receiveDateTimestamp = new Timestamp(parsedDate.getTime());
                    delivery.setReceiveDate(receiveDateTimestamp);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(AdminViewDeliveryDetailActivity.this, AdminViewDeliveryRecipientActivity.class);
            intent.putExtra("DELIVERY_DATA", delivery);

            boolean isEdit = btnUpdate.getVisibility() == View.GONE;
            intent.putExtra("isEdit", isEdit);

            successActivityLauncher.launch(intent);
        });

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                getSharedPreferences("delivery_update_prefs", MODE_PRIVATE).edit().clear().apply();
                getSharedPreferences("delivery_prefs", MODE_PRIVATE).edit().clear().apply();
                finish();
            } else if (result.getResultCode() == RESULT_CANCELED && result.getData() != null) {
                boolean isEdit = (boolean) result.getData().getBooleanExtra("isEdit", false);
                if (isEdit) enableFields();
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

                        String[] statuses = {"Diproses", "Dikirim", "Diterima"};

                        for (String status : statuses) {
                            if (status.equalsIgnoreCase(delivery.getStatus().getStatus())) {
                                statusSpinner.setText(status, false);
                                break;
                            }
                        }

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
                    String date = String.format(Locale.forLanguageTag("id-ID"), "%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);
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

        btnDelete.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_delete_delivery_detail_view) {
            int id = getIntent().getIntExtra("DELIVERY_ID", -1);
            new AlertDialog.Builder(AdminViewDeliveryDetailActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin menghapus data ini?")
                    .setPositiveButton("YES", (dialog, which) -> deleteDelivery(id))
                    .setNegativeButton("NO", null)
                    .show();
        } else if (view.getId() == R.id.btn_update_delivery_detail_view)
            enableFields();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
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
                        new AlertDialog.Builder(AdminViewDeliveryDetailActivity.this)
                                .setTitle("GAGAL")
                                .setMessage(baseResponse.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(AdminViewDeliveryDetailActivity.this)
                            .setTitle("ERROR")
                            .setMessage("Terjadi kesalahan pada sistem kami.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Void>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(AdminViewDeliveryDetailActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}
