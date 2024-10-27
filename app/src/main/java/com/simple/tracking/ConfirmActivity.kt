package com.simple.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

class ConfirmActivity : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var subTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        title = findViewById(R.id.textView3)
        subTitle = findViewById(R.id.textView4)

        val actionType = intent.getStringExtra("ACTION_TYPE")
        val menuName = intent.getStringExtra("MENU_NAME")

        title.text = "${actionType?.uppercase()} ${menuName?.uppercase()}"
        subTitle.text = "Are you sure you want to ${actionType?.lowercase()} this data?"

        val btnBack = findViewById<MaterialCardView>(R.id.btn_no)
        btnBack.setOnClickListener {
            finish()
        }
    }
}