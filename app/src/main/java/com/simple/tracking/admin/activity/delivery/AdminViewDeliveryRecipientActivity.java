package com.simple.tracking.admin.activity.delivery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.LocationChecker;
import com.simple.tracking.R;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.DeliveryRecipient;

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

        Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");
        setValueFields(delivery);

        if (!delivery.getStatus().equalsIgnoreCase("DITERIMA"))
            btnDeleteDeliveryRecipientUpdate.setVisibility(View.GONE);
    }

    public void setValueFields(Delivery delivery) {
        textInputFullnameUpdate.setText(delivery.getRecipient().getName());
        textInputWhatsappUpdate.setText("");
        textInputAddressUpdate.setText(delivery.getRecipient().getAddress().getStreet());
        textInputSubDistrictUpdate.setText(delivery.getRecipient().getAddress().getSubDistrict());
        textInputDistrictUpdate.setText(delivery.getRecipient().getAddress().getDistrict());
        textInputCityUpdate.setText(delivery.getRecipient().getAddress().getCity());
        textInputProvinceUpdate.setText(delivery.getRecipient().getAddress().getProvince());
        textInputPostalCodeUpdate.setText(delivery.getRecipient().getAddress().getPostalCode());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_delivery_recipient_update) finish();
        else if (view.getId() == R.id.btn_save_update_delivery_recipient)
            new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin mengubah data ini?")
                    .setPositiveButton("YES", (dialog, which) -> {})
                    .setNegativeButton("NO", null)
                    .show();
        else if (view.getId() == R.id.btn_delete_delivery_recipient_update)
            new AlertDialog.Builder(AdminViewDeliveryRecipientActivity.this)
                    .setTitle("KONFIRMASI")
                    .setMessage("Apakah anda yakin ingin menghapus data ini?")
                    .setPositiveButton("YES", (dialog, which) -> {})
                    .setNegativeButton("NO", null)
                    .show();
        else if (view.getId() == R.id.btn_update_delivery_recipient_update) {
            btnSaveUpdateDeliveryRecipientUpdate.setVisibility(View.VISIBLE);
            btnUpdateDeliveryRecipientUpdate.setVisibility(View.GONE);
            btnDeleteDeliveryRecipientUpdate.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }
    }
}
