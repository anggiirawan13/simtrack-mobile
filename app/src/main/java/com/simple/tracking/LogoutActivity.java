package com.simple.tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;

public class LogoutActivity extends AppCompatActivity {

    private MaterialCardView btnNo;
    private MaterialCardView btnYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btnNo = findViewById(R.id.btn_no_logout);
        btnNo.setOnClickListener(v -> {
            finish();
        });

        btnYes = findViewById(R.id.btn_yes_logout);
        btnYes.setOnClickListener(v -> {
            PreferenceManager preferenceManager = new PreferenceManager(this);
            preferenceManager.clearData(); // Hapus data saat logout

            getSharedPreferences("delivery_update_prefs", MODE_PRIVATE).edit().clear().apply();
            getSharedPreferences("delivery_prefs", MODE_PRIVATE).edit().clear().apply();
            getSharedPreferences("tracking_prefs", MODE_PRIVATE).edit().clear().apply();

            Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
