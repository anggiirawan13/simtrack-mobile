package com.simple.tracking.admin.activity.shipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.simple.tracking.R

class AdminUpdateShipperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_update_shipper)

        val roles = arrayOf("Admin", "Commissioner", "Director", "Shipper")

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
    }
}