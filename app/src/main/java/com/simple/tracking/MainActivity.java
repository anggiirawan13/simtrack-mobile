package com.simple.tracking;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.admin.activity.AdminActivity;
import com.simple.tracking.admin.activity.ShipperActivity;
import com.simple.tracking.model.Auth;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.LoginAPIConfiguration;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText username;
    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationChecker.checkLocationSettings(this, 1000);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        PreferenceManager preferenceManager = new PreferenceManager(this);

        if (preferenceManager.isLoggedIn()) {
            String userRole = preferenceManager.getUserRole();
            Intent intent;

            if (userRole.equalsIgnoreCase("ADMIN"))
                intent = new Intent(MainActivity.this, AdminActivity.class);
            else
                intent = new Intent(MainActivity.this, ShipperActivity.class);

            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.textInputUsernameLogin);
        password = findViewById(R.id.textInputPasswordLogin);

        CardView btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
//            Auth auth = new Auth();
//            auth.setUsername(Objects.requireNonNull(username.getText()).toString());
//            auth.setPassword(Objects.requireNonNull(password.getText()).toString());
//
//            Call<BaseResponse<Auth>> call = LoginAPIConfiguration.getInstance().login(auth);
//            call.enqueue(new Callback<BaseResponse<Auth>>() {
//                @Override
//                public void onResponse(@NonNull Call<BaseResponse<Auth>> call, @NonNull Response<BaseResponse<Auth>> response) {
//                    BaseResponse<Auth> baseResponse = response.body();
//                    assert baseResponse != null;
//                    if (!baseResponse.isSuccess()) {
//                        new AlertDialog.Builder(MainActivity.this).
//                                setTitle("GAGAL")
//                                .setMessage(baseResponse.getMessage())
//                                .setPositiveButton("OK", null)
//                                .show();
//                    } else {
//                        int userId = baseResponse.getData().getId();
//                        String userRole = baseResponse.getData().getRole();
                        Intent intent;
//                        if (userRole.equalsIgnoreCase("ADMIN")) {
                            intent = new Intent(MainActivity.this, AdminActivity.class);
//                        } else {
//                            intent = new Intent(MainActivity.this, ShipperActivity.class);
//                        }
//
                        PreferenceManager preferenceManager = new PreferenceManager(MainActivity.this);
                        preferenceManager.saveUser(1, "admin");

                        startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<BaseResponse<Auth>> call, @NonNull Throwable t) {
//                    Log.e("API Error", "Terjadi kesalahan pada sistem.");
//                }
//            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                // Lokasi telah diaktifkan, Anda bisa lanjutkan operasi
            } else {
                // Lokasi masih dimatikan, Anda bisa menampilkan pesan atau mengambil tindakan lain
            }
        }
    }
}
