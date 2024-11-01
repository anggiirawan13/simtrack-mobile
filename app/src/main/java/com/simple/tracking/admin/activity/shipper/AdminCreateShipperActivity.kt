package com.simple.tracking.admin.activity.shipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.simple.tracking.ConfirmActivity
import com.simple.tracking.R

class AdminCreateShipperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_shipper)

        val roles = arrayOf("User 1", "User 2", "User 3")

        val roleSpinner = findViewById<MaterialAutoCompleteTextView>(R.id.roleSpinner)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        roleSpinner.setAdapter(adapter)

        roleSpinner.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                roleSpinner.showDropDown()
            }
        }

        roleSpinner.setOnClickListener {
            roleSpinner.showDropDown()
        }

        val btnBack = findViewById<ImageView>(R.id.btn_back_shipper_create)
        btnBack.setOnClickListener {
            finish()
        }

        val btnSave = findViewById<CardView>(R.id.btn_save_create_shipper)
        btnSave.setOnClickListener {
            val intent = Intent(this@AdminCreateShipperActivity, ConfirmActivity::class.java)
            intent.putExtra("ACTION_TYPE", "CREATE")
            intent.putExtra("MENU_NAME", "SHIPPER")
            startActivity(intent)
        }
    }
}