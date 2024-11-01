package com.simple.tracking.admin.activity.delivery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.simple.tracking.R

class AdminUpdateDeliveryRecipientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_update_delivery_recipient)

        val btnBack = findViewById<ImageView>(R.id.btn_back_delivery_recipient_update)
        btnBack.setOnClickListener {
            finish()
        }
    }
}