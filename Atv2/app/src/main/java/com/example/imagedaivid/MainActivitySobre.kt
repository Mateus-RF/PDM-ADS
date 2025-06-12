package com.example.imagedaivid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivitySobre : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_sobre)

        val button_voltar = findViewById<Button>(R.id.button3)

        button_voltar.setOnClickListener{
            val tela_main = Intent(this@MainActivitySobre, MainActivity::class.java)
            startActivity(tela_main)
        }
    }
}