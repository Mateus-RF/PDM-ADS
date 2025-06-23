package com.example.myapplication6

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
import androidx.transition.Visibility

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
        val altura = findViewById<EditText>(R.id.editTextAltura)
        val peso = findViewById<EditText>(R.id.editTextPeso)
        val exercicio = findViewById<EditText>(R.id.editTextExercicio)

        val textResultadoImc = findViewById<TextView>(R.id.textViewResultImc)
        val textExercicio = findViewById<TextView>(R.id.textViewExercicio)

        val button = findViewById<Button>(R.id.buttonCalcular)
        val buttonMostrar = findViewById<Button>(R.id.buttonMostrar)
        val buttonLimpar = findViewById<Button>(R.id.buttonLimpar)
        val buttonDicas = findViewById<Button>(R.id.buttonDicas)
        val buttonSobre = findViewById<Button>(R.id.buttonSobre)

        val video = findViewById<VideoView>(R.id.videoView)
        val uri = Uri.parse("android.resource://$packageName/${R.raw.video_motivacional}")
        video.setVideoURI(uri)
        val image = findViewById<ImageView>(R.id.imageView)


        button.setOnClickListener {
            val peso = peso.text.toString().toFloatOrNull()
            val altura = altura.text.toString().toFloatOrNull()?.div(100f)

            if (peso != null && altura != null) {
                val imc = peso / (altura * altura)
                val classificacao = when {
                    imc < 18.5 -> "Abaixo do peso"
                    imc < 24.9 -> "Peso normal"
                    imc < 29.9 -> "Sobrepeso"
                    else -> "Obesidade"
                }
                textResultadoImc.text = "IMC: %.1f - %s".format(imc, classificacao)
                video.visibility = View.VISIBLE
                video.start()
            }
        }
        buttonLimpar.setOnClickListener {
            altura.text.clear()
            peso.text.clear()
            exercicio.text.clear()
            textExercicio.text = ""
            textResultadoImc.text = ""
            video.suspend()
            video.visibility = View.GONE
            image.visibility = View.GONE
        }

        buttonMostrar.setOnClickListener {
            val exerc = exercicio.text.toString().trim().lowercase()
            when (exerc){
                "agachamento" -> {
                    textExercicio.setText("Agachamento: ativa quadríceps e glúteos")
                    image.setImageResource(R.drawable.agachamento)
                    image.visibility = View.VISIBLE
                }
                "terra" -> {
                    textExercicio.setText("Terra: ativa quadríceps, glúteos.")
                    image.setImageResource(R.drawable.terra)
                    image.visibility = View.VISIBLE
                }
                "supino" -> {
                    textExercicio.setText("Supino: Ativa o Peitoral e o Triceps.")
                    image.setImageResource(R.drawable.supino)
                    image.visibility = View.VISIBLE
                }
                else -> {
                    textExercicio.setText("Exercicio invalido.")
                    image.visibility = View.GONE
                }
            }
        }
        buttonDicas.setOnClickListener {
            val tela = Intent(this, DicasActivity::class.java)
            startActivity(tela)
        }
        buttonSobre.setOnClickListener {
            val tela = Intent(this, BioActivity::class.java)
            startActivity(tela)
        }
    }
}