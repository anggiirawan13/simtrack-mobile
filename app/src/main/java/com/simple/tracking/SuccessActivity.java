package com.simple.tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.simple.tracking.admin.fragment.UserFragment;

public class SuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        subTitle = findViewById(R.id.textView4);

        String actionType = getIntent().getStringExtra("ACTION_TYPE");
        String menuName = getIntent().getStringExtra("MENU_NAME");

        subTitle.setText((menuName != null ? menuName.toLowerCase() : "") +
                " has been successfully " +
                (actionType != null ? actionType.toLowerCase() : "") + ".");

        MaterialCardView btnHome = findViewById(R.id.btn_back_to_home_success);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_to_home_success) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
