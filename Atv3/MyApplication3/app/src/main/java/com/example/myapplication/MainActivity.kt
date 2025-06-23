package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ataque = findViewById<EditText>(R.id.editTextAtaque)
        val defesa = findViewById<EditText>(R.id.editTextDefesa)

        val botao_power = findViewById<Button>(R.id.buttonPower)
        val botao_limpar = findViewById<Button>(R.id.buttonLimpar)
        val botao_sobre = findViewById<Button>(R.id.buttonSobre)
        val botao_persona = findViewById<Button>(R.id.buttonPersona)

        val textview_result = findViewById<TextView>(R.id.textViewResult)
        val imageview = findViewById<ImageView>(R.id.imageView)
        val videoview = findViewById<VideoView>(R.id.videoView)
        val uri = Uri.parse("android.resource://$packageName/${R.raw.videoplayback}")
        videoview.setVideoURI(uri)
        videoview.start()

        botao_power.setOnClickListener {
            val atk = ataque.text.toString().toFloat()
            val def = defesa.text.toString().toFloat()
            val power_total = (atk * 1.5)+(def * 1.2)
            textview_result.setText("Seu poder de ataque é de: $power_total")

            when{
                power_total < 200.0 -> {
                    // Fraco
                    imageview.visibility = View.VISIBLE
                    imageview.setImageResource(R.drawable.goku_normal)
                }
                power_total in 200.0..400.0 -> {
                    // Médio
                    imageview.visibility = View.VISIBLE
                    imageview.setImageResource(R.drawable.goku_ssj1)
                }
                power_total in 400.0..600.0 -> {
                    // forte
                    imageview.visibility = View.VISIBLE
                    imageview.setImageResource(R.drawable.goku_ssj2)
                }
                power_total in 600.0..800.0 -> {
                    // Muito forte
                    imageview.visibility = View.VISIBLE
                    imageview.setImageResource(R.drawable.goku_ssj3)
                }
                power_total >= 800 -> {
                    // Fora da curva
                    imageview.visibility = View.VISIBLE
                    imageview.setImageResource(R.drawable.goku_ssj4)
                }
                else ->{
                    imageview.visibility = View.GONE
                }
            }
        }


        botao_limpar.setOnClickListener {
            ataque.text.clear()
            defesa.text.clear()
            textview_result.setText("")
            imageview.visibility = View.GONE
        }
        botao_persona.setOnClickListener {
            val tela = Intent(this, CharacterActivity::class.java)
            startActivity(tela)
        }

        botao_sobre.setOnClickListener {
            val tela = Intent(this, AboutActivity::class.java)
            startActivity(tela)
        }
    }
}