package com.example.myapplication6

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DicasActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dicas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tecnicas = findViewById<EditText>(R.id.editTextTecnica)
        val button_tecnica = findViewById<Button>(R.id.buttonTecnica)
        val button_voltar = findViewById<Button>(R.id.buttonVoltar)
        val text_result = findViewById<TextView>(R.id.textViewNothin)
        val video_tecnica = findViewById<VideoView>(R.id.videoView2)


        button_tecnica.setOnClickListener {
            val tecnica = tecnicas.text.toString().trim().lowercase()
            when (tecnica){
                "drop-set" -> {
                    val uri = Uri.parse("android.resource://$packageName/${R.raw.videoplayback_1}")
                    video_tecnica.visibility = View.VISIBLE
                    video_tecnica.setVideoURI(uri)
                    video_tecnica.start()
                    text_result.text = ""

                }
                "cluster-set" -> {
                    val uri = Uri.parse("android.resource://$packageName/${R.raw.videoplayback_3}")
                    video_tecnica.visibility = View.VISIBLE
                    video_tecnica.setVideoURI(uri)
                    video_tecnica.start()
                    text_result.text = ""
                }
                "rest/pause" -> {
                    val uri = Uri.parse("android.resource://$packageName/${R.raw.videoplayback_2}")
                    video_tecnica.visibility = View.VISIBLE
                    video_tecnica.setVideoURI(uri)
                    video_tecnica.start()
                    text_result.text = ""
                }
                "bi-set" -> {
                    val uri = Uri.parse("android.resource://$packageName/${R.raw.videoplayback}")
                    video_tecnica.visibility = View.VISIBLE
                    video_tecnica.setVideoURI(uri)
                    video_tecnica.start()
                }
                else -> {
                    text_result.text = "Digite uma dica valida!"
                    video_tecnica.visibility = View.GONE
                }
            }
        }
        button_voltar.setOnClickListener {
            val tela = Intent(this, MainActivity::class.java)
            startActivity(tela)
        }
    }
}