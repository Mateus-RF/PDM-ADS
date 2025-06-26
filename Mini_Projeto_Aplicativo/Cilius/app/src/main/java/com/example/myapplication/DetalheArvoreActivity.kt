package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetalheArvoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_arvore)
        /* --- recupera dados vindos do card, Enviados do EspecieFragment.kt --- */
        val idRecebido   = intent.getIntExtra("id", -1)
        val especie      = intent.getStringExtra("especie")
        val data         = intent.getStringExtra("data")
        val condicao     = intent.getStringExtra("condicao")
        val lat          = intent.getFloatExtra("latitude", 0f)
        val lon          = intent.getFloatExtra("longitude", 0f)
        val image        = intent.getStringExtra("imagem")
        /* --- preenche a tela --- */
        findViewById<TextView>(R.id.textViewEspecie).text = "Espécie: $especie"
        findViewById<TextView>(R.id.textViewData).text    = "Ultimo Registro: $data"
        findViewById<TextView>(R.id.textViewCondicao).text = "Condição: $condicao"
        findViewById<TextView>(R.id.textViewLat).text     = "Latitude: $lat"
        findViewById<TextView>(R.id.textViewLog).text     = "Longitude: $lon"

        val imageView = findViewById<ImageView>(R.id.imageViewEspecie)

        Log.d("Detalhe", "Imagem recebida: $image")

        if (!image.isNullOrBlank()) {
            val imageUri = Uri.parse(image)
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.nature_icon)
                .error(R.drawable.nature_icon)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.nature_icon)
        }
        /* --- botão editar --- */
        findViewById<Button>(R.id.buttonEdit).setOnClickListener {
            val editIntent = Intent(this, MainActivityAddEspecie::class.java).apply {
                putExtra("id", idRecebido)
                putExtra("especie",  especie)
                putExtra("data",     data)
                putExtra("condicao", condicao)
                putExtra("latitude", lat)
                putExtra("longitude", lon)
                putExtra("image", image)
            }
            startActivity(editIntent)
        }
        /* --- botão Relatorio --- */
        findViewById<Button>(R.id.buttonRelatorio).setOnClickListener {
            val telaRelatorio = Intent(this, RelatorioActivity::class.java).apply {
                putExtra(RelatorioActivity.EXTRA_CONDICAO, condicao)
            }
            startActivity(telaRelatorio)
        }
    }
}