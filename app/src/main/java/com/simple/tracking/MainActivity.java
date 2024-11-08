package com.simple.tracking;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.admin.activity.AdminActivity;
import com.simple.tracking.MainActivity;
import com.simple.tracking.admin.activity.ShipperActivity;
import com.simple.tracking.admin.fragment.DeliveryFragment;
import com.simple.tracking.admin.fragment.ShipperFragment;
import com.simple.tracking.admin.fragment.UserFragment;
import com.simple.tracking.model.Auth;
import com.simple.tracking.model.User;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.LoginAPIConfiguration;
import com.simple.tracking.network.UserAPIConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText username;
    private TextInputEditText password;
    private CardView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager preferenceManager = new PreferenceManager(this);

        // Cek apakah sudah login
        if (preferenceManager.isLoggedIn()) {
            String userRole = preferenceManager.getUserRole();
            Intent intent;
            if ("SHIPPER".equalsIgnoreCase(userRole)) {
                intent = new Intent(MainActivity.this, ShipperActivity.class);
            } else {
                intent = new Intent(MainActivity.this, AdminActivity.class);
            }
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.textInputUsernameLogin);
        password = findViewById(R.id.textInputPasswordLogin);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
//            Auth auth = new Auth();
//            auth.setUsername(username.getText().toString());
//            auth.setPassword(password.getText().toString());
//
//            Call<BaseResponse<Auth>> call = LoginAPIConfiguration.getInstance().login(auth); // Updated to match the new return type
//            call.enqueue(new Callback<BaseResponse<Auth>>() {
//                @Override
//                public void onResponse(Call<BaseResponse<Auth>> call, Response<BaseResponse<Auth>> response) {
//                    if (response.isSuccessful()) {
//                        BaseResponse<Auth> baseResponse = response.body();
//                        if (baseResponse != null && baseResponse.isSuccess()) {
//                            String userRole = baseResponse.getData().getRole();
                            Intent intent;
//                            if (userRole.equalsIgnoreCase("SHIPPER")) {
//                                intent = new Intent(MainActivity.this, ShipperActivity.class);
//                            } else {
                                intent = new Intent(MainActivity.this, AdminActivity.class);
//                            }
//
                            PreferenceManager preferenceManager = new PreferenceManager(this);
                            preferenceManager.saveUser(74, "admin");
                            startActivity(intent);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Username atau Password Salah!", Toast.LENGTH_SHORT).show();
//                            Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
//                        }
//                    } else {
//                        Toast.makeText(MainActivity.this, "Terjadi kesalahan pada sistem.", Toast.LENGTH_SHORT).show();
//                        Log.e("API Error", "Response not successful: " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<BaseResponse<Auth>> call, Throwable t) {
//                    Log.e("API Error", "Terjadi kesalahan pada sistem.");
//                }
//            });
        }
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
