package com.simple.tracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SuccessActivity : AppCompatActivity() {

    private lateinit var subTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        subTitle = findViewById(R.id.textView4)

        val actionType = intent.getStringExtra("ACTION_TYPE")
        val menuName = intent.getStringExtra("MENU_NAME")

        subTitle.text = "${menuName?.lowercase()} has been successfully ${actionType?.lowercase()}."
    }
}