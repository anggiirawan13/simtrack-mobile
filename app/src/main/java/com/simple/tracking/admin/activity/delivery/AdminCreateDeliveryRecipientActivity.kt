package com.simple.tracking.admin.activity.delivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.simple.tracking.ConfirmActivity
import com.simple.tracking.R

class AdminCreateDeliveryRecipientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_delivery_recipient)

        val btnBack = findViewById<ImageView>(R.id.btn_back_detail)
        btnBack.setOnClickListener {
            val intent = Intent(this@AdminCreateDeliveryRecipientActivity, AdminCreateDeliveryDetailActivity::class.java);
            startActivity(intent);
        }

        val btnSave = findViewById<CardView>(R.id.btn_save_delivery)
        btnSave.setOnClickListener {
            val intent = Intent(this@AdminCreateDeliveryRecipientActivity, ConfirmActivity::class.java)
            intent.putExtra("ACTION_TYPE", "CREATE")
            intent.putExtra("MENU_NAME", "DELIVERY")
            startActivity(intent)
        }
    }
}