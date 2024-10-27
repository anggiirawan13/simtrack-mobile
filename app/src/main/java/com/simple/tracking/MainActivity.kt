package com.simple.tracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.simple.tracking.admin.activity.AdminActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<TextView>(R.id.textView2)
        btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.textView2 -> {
                val adminActivity =Intent(this@MainActivity, AdminActivity::class.java)
                adminActivity.putExtra("DEFAULT_TAB", 1)
                startActivity(adminActivity)
            }
        }
    }
}