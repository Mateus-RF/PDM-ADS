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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivityProfessores : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_professores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val texto_entrada = findViewById<EditText>(R.id.editTextText2)
        val text_saida = findViewById<TextView>(R.id.textView5)
        val imagem_saulo = findViewById<ImageView>(R.id.imageView2)
        val button_back = findViewById<Button>(R.id.button_back)
        val button_confirmar = findViewById<Button>(R.id.button_confirmar)

        val lista_professores = listOf("saulo", "samuel", "julio",
            "reginaldo", "anelise", "jussara", "lucas", "phyllipe",
            "erico", "patricia", "getulio", "willame")

        button_confirmar.setOnClickListener {
            val nome_digitado = texto_entrada.text.toString().trim().lowercase()

            if (nome_digitado in lista_professores) {
                text_saida.text = "Professor válido"

                if (nome_digitado == "saulo") {
                    text_saida.text = "Saulo mexicano"
                    imagem_saulo.visibility = View.VISIBLE
                } else {
                    imagem_saulo.visibility = View.GONE
                }

            } else {
                text_saida.text = "Professor inválido, digite novamente!"
                imagem_saulo.visibility = View.GONE
            }
        }

        button_back.setOnClickListener {
            val tela_main = Intent(this@MainActivityProfessores, MainActivity::class.java)
            startActivity(tela_main)
        }

    }
}