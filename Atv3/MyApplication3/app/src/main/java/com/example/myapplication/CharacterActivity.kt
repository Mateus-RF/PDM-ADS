package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CharacterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_character)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val personagem = findViewById<EditText>(R.id.editTextPersona)
        val button_mostrar = findViewById<Button>(R.id.buttonMostrar)
        val button_back = findViewById<Button>(R.id.buttonVoltar)
        val textview_nothing = findViewById<TextView>(R.id.textViewNothing)
        val imageview = findViewById<ImageView>(R.id.imageView3)

        button_mostrar.setOnClickListener {
            val persona = personagem.text.toString().trim().lowercase()
            when (persona){
                "ichigo" ->{
                    imageview.setImageResource(R.drawable.ichigo)
                    imageview.visibility = View.VISIBLE
                    textview_nothing.setText("")
                }
                "rayne" ->{
                    imageview.setImageResource(R.drawable.rayne)
                    imageview.visibility = View.VISIBLE
                    textview_nothing.setText("")
                }
                "toji" ->{
                    imageview.setImageResource(R.drawable.toji)
                    imageview.visibility = View.VISIBLE
                    textview_nothing.setText("")
                }
                else ->{
                    textview_nothing.setText("Personagem não encontrado! Tente novamente.")
                    imageview.visibility = View.GONE
                }
            }
        }
        button_back.setOnClickListener {
            val telaHome = Intent(this, MainActivity::class.java)
            startActivity(telaHome)
        }


    }
}