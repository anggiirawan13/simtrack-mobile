package com.simple.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ConfirmActivity : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var subTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        title = findViewById(R.id.textView3)
        subTitle = findViewById(R.id.textView4)

        val actionType = intent.getStringExtra("MENU_NAME")
        val menuName = intent.getStringExtra("MENU_NAME")

        title.text = "${actionType?.uppercase()} ${menuName?.uppercase()}"
        subTitle.text = "Are you sure you want to ${actionType?.lowercase()} this data?"
    }
}