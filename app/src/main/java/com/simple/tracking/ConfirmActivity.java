package com.simple.tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import com.simple.tracking.admin.activity.delivery.AdminCreateDeliveryRecipientActivity;
import com.simple.tracking.admin.activity.shipper.AdminCreateShipperActivity;
import com.simple.tracking.admin.activity.shipper.AdminViewShipperActivity;
import com.simple.tracking.admin.activity.user.AdminCreateUserActivity;
import com.simple.tracking.admin.activity.user.AdminViewUserActivity;
import com.simple.tracking.model.Delivery;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.User;

public class ConfirmActivity extends AppCompatActivity {

    private TextView title;
    private TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        title = findViewById(R.id.textView3);
        subTitle = findViewById(R.id.textView4);

        String actionType = getIntent().getStringExtra("ACTION_TYPE");
        String menuName = getIntent().getStringExtra("MENU_NAME");

        title.setText((actionType != null ? actionType.toUpperCase() : "") + " " +
                (menuName != null ? menuName.toUpperCase() : ""));
        subTitle.setText("Are you sure you want to " +
                (actionType != null ? actionType.toLowerCase() : "") + " this data?");

        MaterialCardView btnYes = findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            if (menuName != null) {
                switch (menuName.toUpperCase()) {
                    case "USER":
                            if (actionType.equals("CREATE")) {
                                User user = (User) getIntent().getSerializableExtra("USER_DATA");
                                AdminCreateUserActivity create = new AdminCreateUserActivity();
                                create.createUser(user);  // Pastikan untuk mengelola panggilan ini dengan baik
                            } else if (actionType.equals("UPDATE")) {
                                User user = (User) getIntent().getSerializableExtra("USER_DATA");
                                AdminViewUserActivity view = new AdminViewUserActivity();
                                view.updateUser(user.getId(), user);
                            } else if (actionType.equals("DELETE")) {
                                int id = getIntent().getIntExtra("USER_ID", 1);

                                AdminViewUserActivity view = new AdminViewUserActivity();
                                view.deleteUser(id);
                            }
                        break;
                    case "SHIPPER":
                        if (actionType.equals("CREATE")) {
                            Shipper shipper = (Shipper) getIntent().getSerializableExtra("SHIPPER_DATA");
                            AdminCreateShipperActivity create = new AdminCreateShipperActivity();
                            create.createShipper(shipper);  // Pastikan untuk mengelola panggilan ini dengan baik
                        } else if (actionType.equals("UPDATE")) {
                            Shipper shipper = (Shipper) getIntent().getSerializableExtra("SHIPPER_DATA");
                            AdminViewShipperActivity view = new AdminViewShipperActivity();
                            view.updateShipper(shipper.getId(), shipper);
                        } else if (actionType.equals("DELETE")) {
                            int id = getIntent().getIntExtra("SHIPPER_ID", 1);

                            AdminViewShipperActivity view = new AdminViewShipperActivity();
                            view.deleteShipper(id);
                        }
                        break;
                    case "DELIVERY":
                        if (actionType.equals("CREATE")) {
                            Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");
                            AdminCreateDeliveryRecipientActivity create = new AdminCreateDeliveryRecipientActivity();
                            SharedPreferences prefs = getSharedPreferences("delivery_prefs", MODE_PRIVATE);
                            prefs.edit().clear().apply();
                            create.createDelivery(delivery);  // Pastikan untuk mengelola panggilan ini dengan baik
                        } else if (actionType.equals("UPDATE")) {
                            Delivery delivery = (Delivery) getIntent().getSerializableExtra("DELIVERY_DATA");
                            AdminCreateDeliveryRecipientActivity view = new AdminCreateDeliveryRecipientActivity();
//                            view.updateDelivery(delivery.getId(), delivery);
                        } else if (actionType.equals("DELETE")) {
                            int id = getIntent().getIntExtra("DELIVERY_ID", 1);

                            AdminCreateDeliveryRecipientActivity view = new AdminCreateDeliveryRecipientActivity();
//                            view.deleteDelivery(id);
                        }
                        break;
                }
            }

            setResult(RESULT_OK);
            finish();
        });

        MaterialCardView btnNo = findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> finish());
    }
}
