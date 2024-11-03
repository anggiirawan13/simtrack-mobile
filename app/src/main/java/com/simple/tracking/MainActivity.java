package com.simple.tracking;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.simple.tracking.admin.activity.AdminActivity;
import com.simple.tracking.admin.fragment.DeliveryFragment;
import com.simple.tracking.admin.fragment.ShipperFragment;
import com.simple.tracking.admin.fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btn = findViewById(R.id.textView2);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> successActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String menuName = result.getData().getStringExtra("MENU_NAME");
                    if (menuName != null) {
                        switch (menuName) {
                            case "USER":
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new UserFragment())
                                        .commit();
                                break;
                            case "DELIVERY":
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new DeliveryFragment())
                                        .commit();
                                break;
                            case "SHIPPER":
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ShipperFragment())
                                        .commit();
                                break;
                        }
                    }
                }
            });

}
