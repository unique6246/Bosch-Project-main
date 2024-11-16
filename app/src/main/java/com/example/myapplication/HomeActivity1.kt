package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class HomeActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val second=findViewById<ImageButton>(R.id.button)
        second.setOnClickListener{
            val intent=Intent(this,ScanToolActivity2::class.java)
            startActivity(intent)
        }

    }
}