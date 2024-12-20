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
            Auth auth = new Auth();
            auth.setUsername(Objects.requireNonNull(username.getText()).toString());
            auth.setPassword(Objects.requireNonNull(password.getText()).toString());

            Call<BaseResponse<User>> call = LoginAPIConfiguration.getInstance().login(auth);
            call.enqueue(new Callback<BaseResponse<User>>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                    BaseResponse<User> baseResponse = response.body();
                    assert baseResponse != null;
                    if (!baseResponse.isSuccess()) {
                        new AlertDialog.Builder(MainActivity.this).
                                setTitle("GAGAL")
                                .setMessage(baseResponse.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        int userId = baseResponse.getData().getId();
                        String userRole = baseResponse.getData().getRole().getRole();
                        Intent intent;
                        if (userRole.equalsIgnoreCase("ADMIN") || userRole.equalsIgnoreCase("ADMINISTRATOR")) {
                            intent = new Intent(MainActivity.this, AdminActivity.class);
                        } else {
                            getFirebaseToken(userId);
                            intent = new Intent(MainActivity.this, ShipperActivity.class);
                        }

                        PreferenceManager preferenceManager = new PreferenceManager(MainActivity.this);
                        preferenceManager.saveUser(userId, userRole);

                        getFirebaseToken(userId);

                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable t) {
                    Log.e("API Error", "Terjadi kesalahan pada sistem.");
                }
            });
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

    public String getAndroidCodename() {
        int apiLevel = android.os.Build.VERSION.SDK_INT;

        switch (apiLevel) {
            case 33:
                return "Tiramisu"; // Android 13
            case 32:
                return "Snow Cone v2"; // Android 12L
            case 31:
                return "Snow Cone"; // Android 12
            case 30:
                return "Red Velvet Cake"; // Android 11
            case 29:
                return "Quince Tart"; // Android 10
            case 28:
                return "Pie"; // Android 9
            case 27:
            case 26:
                return "Oreo"; // Android 8.0/8.1
            case 25:
                return "Nougat"; // Android 7.1
            case 24:
                return "Nougat"; // Android 7.0
            case 23:
                return "Marshmallow"; // Android 6.0
            case 22:
                return "Lollipop"; // Android 5.1
            case 21:
                return "Lollipop"; // Android 5.0
            case 20:
                return "KitKat Watch"; // Android 4.4W
            case 19:
                return "KitKat"; // Android 4.4
            case 18:
                return "Jelly Bean"; // Android 4.3
            case 17:
                return "Jelly Bean"; // Android 4.2
            case 16:
                return "Jelly Bean"; // Android 4.1
            case 15:
            case 14:
                return "Ice Cream Sandwich"; // Android 4.0
            case 13:
                return "Honeycomb"; // Android 3.2
            case 12:
                return "Honeycomb"; // Android 3.1
            case 11:
                return "Honeycomb"; // Android 3.0
            case 10:
                return "Gingerbread"; // Android 2.3.3
            case 9:
                return "Gingerbread"; // Android 2.3
            case 8:
                return "Froyo"; // Android 2.2
            case 7:
                return "Eclair"; // Android 2.1
            case 6:
                return "Eclair"; // Android 2.0.1
            case 5:
                return "Eclair"; // Android 2.0
            case 4:
                return "Donut"; // Android 1.6
            case 3:
                return "Cupcake"; // Android 1.5
            case 2:
                return "Petit Four"; // Android 1.1
            case 1:
                return "Base"; // Android 1.0
            default:
                return "Unknown";
        }
    }

}
