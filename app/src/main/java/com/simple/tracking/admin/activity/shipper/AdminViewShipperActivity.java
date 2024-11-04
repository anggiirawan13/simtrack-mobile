package com.simple.tracking.admin.activity.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.simple.tracking.ConfirmActivity;
import com.simple.tracking.R;
import com.simple.tracking.admin.adapter.ShipperAdapter;
import com.simple.tracking.model.Shipper;
import com.simple.tracking.network.BaseResponse;
import com.simple.tracking.network.ShipperAPIConfiguration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewShipperActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialAutoCompleteTextView textInputUserIdView;
    private TextInputEditText textInputDeviceMappingView;

    private ActivityResultLauncher<Intent> successActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_shipper); // Use your actual layout name

        // Initialize views
        textInputUserIdView = findViewById(R.id.textInputUserIdView);
        textInputDeviceMappingView = findViewById(R.id.textInputDeviceMappingView);

        ImageView btnBack = findViewById(R.id.btn_back_view_shipper);
        btnBack.setOnClickListener(v -> finish());

        CardView btnDelete = findViewById(R.id.btn_delete_view_shipper);
        btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewShipperActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "DELETE");
            intent.putExtra("MENU_NAME", "SHIPPER");
            intent.putExtra("SHIPPER_ID", getIntent().getIntExtra("SHIPPER_ID", 1));
            successActivityLauncher.launch(intent);
        });

        CardView btnSave = findViewById(R.id.btn_save_view_shipper);
        btnSave.setOnClickListener(this);

        CardView btnUpdate = findViewById(R.id.btn_update_view_shipper);
        btnUpdate.setOnClickListener(v -> {
            enableFields(true);

            btnDelete.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        });

        successActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Arahkan kembali ke ShipperFragment
                setResult(RESULT_OK);
                finish(); // Menghentikan AdminViewShipperActivity
            }
        });

        int id = getIntent().getIntExtra("SHIPPER_ID", 1);
        getShippers(id);
    }

    public void getShippers(int id) {
        Call<BaseResponse<Shipper>> call = ShipperAPIConfiguration.getInstance().getShipper(id); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<Shipper>>() {
            @Override
            public void onResponse(Call<BaseResponse<Shipper>> call, Response<BaseResponse<Shipper>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Shipper> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        Shipper shipper = baseResponse.getData();
                        textInputUserIdView.setText(String.valueOf(shipper.getUserId())); // Assuming getter is getFullname()
                        textInputDeviceMappingView.setText(shipper.getDeviceMapping()); // Assuming getter is getShippername()
                    } else {
                        Toast.makeText(AdminViewShipperActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminViewShipperActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Shipper>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch shippers: " + t.getMessage());
            }
        });
    }

    private void enableFields(boolean enable) {
        textInputUserIdView.setEnabled(enable);
        textInputDeviceMappingView.setEnabled(enable);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_view_shipper) {
            Shipper shipper = new Shipper();
            shipper.setId(getIntent().getIntExtra("SHIPPER_ID", 1));
            shipper.setUserId(Integer.parseInt(textInputUserIdView.getText().toString()));
            shipper.setDeviceMapping(textInputDeviceMappingView.getText().toString());

            Intent intent = new Intent(AdminViewShipperActivity.this, ConfirmActivity.class);
            intent.putExtra("ACTION_TYPE", "UPDATE");
            intent.putExtra("MENU_NAME", "SHIPPER");
            intent.putExtra("SHIPPER_DATA", shipper);  // Mengirimkan objek Shipper
            successActivityLauncher.launch(intent);
        }
    }

    public void updateShipper(int id, Shipper shipper) {
        Call<BaseResponse<Shipper>> call = ShipperAPIConfiguration.getInstance().updateShipper(id, shipper); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<Shipper>>() {
            @Override
            public void onResponse(Call<BaseResponse<Shipper>> call, Response<BaseResponse<Shipper>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Shipper> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminViewShipperActivity.this, "Shipper Gagal Diubah", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminViewShipperActivity.this, "Shipper Gagal Diubah", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successfulGAGAL: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Shipper>> call, Throwable t) {
                Log.e("API Error", "Shipper Gagal Diubah");
            }
        });
    }

    public void deleteShipper(int id) {
        Call<BaseResponse<Void>> call = ShipperAPIConfiguration.getInstance().deleteShipper(id); // Updated to match the new return type
        call.enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Void> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AdminViewShipperActivity.this, "Shipper Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "API call was not successful: " + (baseResponse != null ? baseResponse.isSuccess() : "No response"));
                    }
                } else {
                    Toast.makeText(AdminViewShipperActivity.this, "Shipper Gagal Dihapus", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response not successfulGAGAL: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Log.e("API Error", "Shipper Gagal Dihapus");
            }
        });
    }
}
