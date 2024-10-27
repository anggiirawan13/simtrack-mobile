package com.simple.tracking.admin.activity.delivery

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.simple.tracking.R
import java.util.Calendar

class AdminViewDeliveryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_delivery_detail)

        val roles = arrayOf("Diproses", "Dikirim", "Diterima")

        val roleSpinner = findViewById<MaterialAutoCompleteTextView>(R.id.roleSpinner)
        val adapterRole =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        roleSpinner.setAdapter(adapterRole)

        roleSpinner.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                roleSpinner.showDropDown()
            }
        }

        roleSpinner.setOnClickListener {
            roleSpinner.showDropDown()
        }

        val shippers = arrayOf("Shipper 1", "Shipper 2", "Shipper 3")

        val shipperSpinner = findViewById<MaterialAutoCompleteTextView>(R.id.roleSpinner2)
        val adapterShipper =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, shippers)
        shipperSpinner.setAdapter(adapterShipper)

        shipperSpinner.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                shipperSpinner.showDropDown()
            }
        }

        shipperSpinner.setOnClickListener {
            shipperSpinner.showDropDown()
        }

        val editTextDate2 = findViewById<EditText>(R.id.editTextDate2)

        editTextDate2.setOnClickListener {
            showDatePickerDialog(editTextDate2)
        }

        val editTextDate3 = findViewById<EditText>(R.id.editTextDate3)

        editTextDate3.setOnClickListener {
            showDatePickerDialog(editTextDate3)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}