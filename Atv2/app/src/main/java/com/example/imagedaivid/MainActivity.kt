package com.example.imagedaivid

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

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val textoEntrada = findViewById<EditText>(R.id.editTextText)
        val button = findViewById<Button>(R.id.button)
        val textoSaida = findViewById<TextView>(R.id.textViewResult)
        val imageViewTaca = findViewById<ImageView>(R.id.imageView)
        val button_limpar = findViewById<Button>(R.id.button_limpar)
        val button_sobre = findViewById<Button>(R.id.button_sobre)
        val button_prof = findViewById<Button>(R.id.button_prof)


        button.setOnClickListener{
            val time = textoEntrada.text.toString().uppercase()
            var mundiais: Int? = null
            var erro = ""
            when (time){
                "SANTOS" -> {
                    mundiais = 2
                }
                "FLAMENGO" -> {
                    mundiais = 1
                }
                "CORINTHIANS" -> {
                    mundiais = 1
                }
                "GREMIO" -> {
                    mundiais = 1
                }
                "SAO PAULO" -> {
                    mundiais = 2
                }
                "INTERNACIONAL" -> {
                    mundiais = 1
                }
                else -> {
                    mundiais = 0
                }
            }
            textoSaida.text = "O time ${time.lowercase().replaceFirstChar { it.uppercase() }} tem $mundiais mundial(is)."

            if (mundiais > 0) {
                imageViewTaca.visibility = View.VISIBLE
            } else {
                imageViewTaca.visibility = View.GONE
            }
        }

        button_limpar.setOnClickListener{
            textoEntrada.setText("")
            textoSaida.setText("")
            imageViewTaca.visibility = View.GONE
        }

        button_sobre.setOnClickListener {
            val tela_sobre = Intent(this@MainActivity, MainActivitySobre::class.java)
            startActivity(tela_sobre)
        }

        button_prof.setOnClickListener {
            val tela_professores = Intent(this@MainActivity, MainActivityProfessores::class.java )
            startActivity(tela_professores)
        }

    }
}