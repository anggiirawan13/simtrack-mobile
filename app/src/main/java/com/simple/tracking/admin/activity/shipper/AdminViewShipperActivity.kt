package com.simple.tracking.admin.activity.shipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.simple.tracking.ConfirmActivity
import com.simple.tracking.R
import com.simple.tracking.admin.activity.user.AdminUpdateUserActivity

class AdminViewShipperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_shipper)

        val btnBack = findViewById<ImageView>(R.id.btn_back_shipper_view)
        btnBack.setOnClickListener {
            finish()
        }

        val btnDelete = findViewById<CardView>(R.id.btn_delete_shipper)
        btnDelete.setOnClickListener {
            val intent = Intent(this@AdminViewShipperActivity, ConfirmActivity::class.java)
            intent.putExtra("ACTION_TYPE", "DELETE")
            intent.putExtra("MENU_NAME", "SHIPPER")
            startActivity(intent)
        }

        val btnUpdate = findViewById<CardView>(R.id.btn_update_shipper)
        btnUpdate.setOnClickListener {
            val intent = Intent(this@AdminViewShipperActivity, AdminUpdateUserActivity::class.java)
            startActivity(intent)
        }
    }
}