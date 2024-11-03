package com.simple.tracking.admin.activity.delivery;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.R;

public class AdminViewDeliveryRecipientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_delivery_recipient);

        ImageView btnBack = findViewById(R.id.btn_back_delivery_recipient_view);
        btnBack.setOnClickListener(v -> finish());

        CardView btnDelete = findViewById(R.id.btn_delete_delivery_recipient_view);
        btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewDeliveryRecipientActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "DELETE");
            intent.putExtra("MENU_NAME", "DELIVERY");
            startActivity(intent);
        });

        CardView btnUpdate = findViewById(R.id.btn_update_delivery_recipient_view);
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewDeliveryRecipientActivity.this, AdminUpdateDeliveryDetailActivity.class);
            startActivity(intent);
        });
    }
}
