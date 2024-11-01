package com.simple.tracking.admin.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.simple.tracking.ConfirmActivity
import com.simple.tracking.R
import com.simple.tracking.admin.adapter.FragmentPagerAdapter
import com.simple.tracking.admin.fragment.UserFragment

class AdminCreateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_user)

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

        val btnBack = findViewById<ImageView>(R.id.btn_back_create_user)
        btnBack.setOnClickListener {
            finish()
        }

        val btnUpdate = findViewById<CardView>(R.id.btn_save_create_user)
        btnUpdate.setOnClickListener {
            val intent = Intent(this@AdminCreateUserActivity, ConfirmActivity::class.java)
            intent.putExtra("ACTION_TYPE", "CREATE")
            intent.putExtra("MENU_NAME", "USER")
            startActivity(intent)
        }
    }
}