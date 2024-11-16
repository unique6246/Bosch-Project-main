package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ToolsListActivity7 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main8)
        val fourth=findViewById<Button>(R.id.settings_button)
        fourth.setOnClickListener {
            val intent= Intent(this,SettingsActivity::class.java)
            startActivity(intent)

        }
        val fourth2=findViewById<Button>(R.id.home_button)
        fourth2.setOnClickListener {
            finish()

        }
    }
}