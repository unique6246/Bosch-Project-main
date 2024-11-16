package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ScanToolActivity2 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val second = findViewById<ImageButton>(R.id.scan_button)
        second.setOnClickListener {
            val intent1 = Intent(this, ScanningActivity3::class.java)
            startActivity(intent1)
        }
        val third = findViewById<ImageButton>(R.id.tools_button)
        third.setOnClickListener {
            val intent2 = Intent(this, ToolsListActivity7::class.java)
            startActivity(intent2)
        }
        val fourth=findViewById<Button>(R.id.settings_button)
        fourth.setOnClickListener {
            val intent=Intent(this,SettingsActivity::class.java)
            startActivity(intent)

        }
    }
}