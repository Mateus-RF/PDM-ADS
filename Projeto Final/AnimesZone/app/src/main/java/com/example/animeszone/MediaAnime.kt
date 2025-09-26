package com.example.animeszone

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaAnime : AppCompatActivity() {

    private val repository = AnimeRepository() // nosso repositório Firebase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media_anime)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recebe dados via intent
        val titlePut = intent.getStringExtra("title")
        val cover = intent.getStringExtra("coverUrl")
        val banner = intent.getStringExtra("bannerUrl")
        val id = intent.getStringExtra("id")
        val descr = intent.getStringExtra("description")

        val spinner = findViewById<Spinner>(R.id.spinner)
        val coverImage = findViewById<ImageView>(R.id.coverImageView)
        val bannerImage = findViewById<ImageView>(R.id.bannerImageView)
        val description = findViewById<TextView>(R.id.descriptionView)
        val title = findViewById<TextView>(R.id.titleView)
        val buttonFav = findViewById<ImageButton>(R.id.imageButton2)

        title.text = titlePut
        description.text = descr
        Glide.with(this).load(cover).into(coverImage)
        Glide.with(this).load(banner).into(bannerImage)

        val animeId = id?.toIntOrNull() ?: return
        var ativado = false

        // Busca se já é favorito no Firestore
        lifecycleScope.launch {
            val favorito = repository.getFavoriteAnimeById(animeId)
            ativado = favorito
            buttonFav.setImageResource(
                if (ativado) R.drawable.ic_fav_on else R.drawable.ic_fav_off
            )
        }

        val condicoes = listOf("Add to List", "Completed", "Watching", "Plan to Watch")

        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, condicoes) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.WHITE)
                return view
            }
        }
        spinner.adapter = adapter

        buttonFav.setOnClickListener {
            ativado = !ativado
            buttonFav.setImageResource(
                if (ativado) R.drawable.ic_fav_on else R.drawable.ic_fav_off
            )

            lifecycleScope.launch {
                val existing = repository.getAnimeById(animeId)
                val selectedStatus = condicoes[0]
                val anime = AnimeEntity(
                    id = animeId,
                    titleRomaji = titlePut ?: "",
                    coverImageUrl = cover,
                    bannerUrl = banner,
                    description = descr,
                    status = selectedStatus,
                    isFavorite = ativado
                )

                if (existing == null) {
                    repository.insertAnime(anime)
                } else {
                    repository.setFavorite(animeId, ativado)
                }
            }
        }

        var primeiraSelecao = true
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, idItem: Long) {
                if (primeiraSelecao) {
                    primeiraSelecao = false
                    return
                }

                val selectedStatus = condicoes[position]

                lifecycleScope.launch {
                    val existing = repository.getAnimeById(animeId)
                    val anime = AnimeEntity(
                        id = animeId,
                        titleRomaji = titlePut ?: "",
                        coverImageUrl = cover,
                        bannerUrl = banner,
                        description = descr,
                        status = selectedStatus,
                        isFavorite = ativado
                    )

                    if (existing == null) {
                        repository.insertAnime(anime)
                    } else {
                        repository.updateStatus(animeId, selectedStatus)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        lifecycleScope.launch {
            val statusAtual = repository.getStatusById(animeId)
            val pos = condicoes.indexOf(statusAtual)
            if (pos >= 0) spinner.setSelection(pos)
        }
    }
}