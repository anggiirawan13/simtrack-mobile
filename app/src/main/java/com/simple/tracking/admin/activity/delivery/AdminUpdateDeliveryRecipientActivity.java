package com.simple.tracking.admin.activity.delivery;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.simple.tracking.R;

public class AdminUpdateDeliveryRecipientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_delivery_recipient);

        ImageView btnBack = findViewById(R.id.btn_back_delivery_recipient_update);
        btnBack.setOnClickListener(v -> finish());
    }
}
