package com.simple.tracking;

import android.Manifest;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.simple.tracking.admin.activity.AdminActivity;
import com.simple.tracking.admin.activity.ShipperActivity;
import com.simple.tracking.MainActivity;
import com.simple.tracking.admin.activity.user.AdminViewUserActivity;
import com.simple.tracking.model.Auth;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.LoginAPIConfiguration;
import com.simple.tracking.network.ShipperAPIConfiguration;
import com.simple.tracking.network.UserAPIConfiguration;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText username;
    private TextInputEditText password;

//    private ActivityResultLauncher<String> resultLauncher = registerForActivityResult(
//            new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    // Permission granted
//                    // Get device token from firebase
//                    getFirebaseToken();
//                } else {
//                    // Permission denied
//                }
//            }
//    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestPermission();

        LocationChecker.checkLocationSettings(this, 1000);

        if (!LocationChecker.isLocationEnabled(this)) {
            LocationChecker.showLocationDisabledDialog(this);
        }

        PreferenceManager preferenceManager = new PreferenceManager(this);
        if (preferenceManager.isLoggedIn()) {
            Intent intent;

            if (preferenceManager.isAdmin()) intent = new Intent(MainActivity.this, AdminActivity.class);
            else intent = new Intent(MainActivity.this, ShipperActivity.class);

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
//                        if (userRole.equalsIgnoreCase("ADMIN") || userRole.equalsIgnoreCase("ADMINISTRATOR")) {
                            intent = new Intent(MainActivity.this, AdminActivity.class);
//                        } else {
//                            intent = new Intent(MainActivity.this, ShipperActivity.class);
//                        }
//
                        PreferenceManager preferenceManager = new PreferenceManager(MainActivity.this);
                        preferenceManager.saveUser(1, "ADMIN");

                        getFirebaseToken(1);

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1000) {
//            if (resultCode == RESULT_OK) {
//                // Lokasi telah diaktifkan, Anda bisa lanjutkan operasi
//            } else {
//                // Lokasi masih dimatikan, Anda bisa menampilkan pesan atau mengambil tindakan lain
//            }
//        }
//    }

//    public void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//                // Permission already granted
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                // You can explain user that why do you need permission (showing dialog or toast message)
//            } else {
//                // Request permission
//                resultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
//            }
//        } else {
//            // Get device token from firebase
//            getFirebaseToken();
//        }
//    }

    public void getFirebaseToken(int shipperId) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("FIREBASE GAGAL")
                            .setMessage(Objects.requireNonNull(task.getException()).getMessage())
                            .setPositiveButton("OK", null)
                            .show();

                    return;
                }

                String token = task.getResult();
                Log.i("Firebase Token", token);
                updateDeviceMapping(shipperId, token);
            }
        });
    }

    public void updateDeviceMapping(int shipperId, String token) {
        Shipper shipper = new Shipper.Builder()
                .setDeviceMapping(token)
                .build();
        Call<BaseResponse<Void>> call = ShipperAPIConfiguration.getInstance().updateDeviceMapping(shipperId, shipper);
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Void>> call, @NonNull Response<BaseResponse<Void>> response) {
                BaseResponse<Void> baseResponse = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Void>> call, @NonNull Throwable t) {
                new AlertDialog.Builder(MainActivity.this).
                        setTitle("ERROR")
                        .setMessage("Terjadi kesalahan pada sistem kami.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

}
