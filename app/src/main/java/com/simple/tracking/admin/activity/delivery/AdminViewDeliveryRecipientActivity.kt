package com.simple.tracking.admin.activity.delivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.simple.tracking.ConfirmActivity
import com.simple.tracking.R

class AdminViewDeliveryRecipientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_delivery_recipient)

        val btnBack = findViewById<ImageView>(R.id.btn_back_delivery_recipient_view)
        btnBack.setOnClickListener {
            finish()
        }

        val btnDelete = findViewById<CardView>(R.id.btn_delete_delivery_recipient_view)
        btnDelete.setOnClickListener {
            val intent = Intent(this@AdminViewDeliveryRecipientActivity, ConfirmActivity::class.java)
            intent.putExtra("ACTION_TYPE", "DELETE")
            intent.putExtra("MENU_NAME", "DELIVERY")
            startActivity(intent)
        }

        val btnUpdate = findViewById<CardView>(R.id.btn_update_delivery_recipient_view)
        btnUpdate.setOnClickListener {
            val intent = Intent(this@AdminViewDeliveryRecipientActivity, AdminUpdateDeliveryDetailActivity::class.java)
            startActivity(intent)
        }
    }
}