package com.example.animeszone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SimpleAnimeAdapter
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewAnimes)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = SimpleAnimeAdapter(mutableListOf()) { animeSelecionado ->
            val intent = Intent(this, MediaAnime::class.java)
            intent.putExtra("title", animeSelecionado.titleRomaji)
            intent.putExtra("coverUrl", animeSelecionado.coverImageUrl)
            intent.putExtra("bannerUrl", animeSelecionado.bannerUrl)  // novo campo
            intent.putExtra("id", animeSelecionado.id.toString())
            intent.putExtra("description", animeSelecionado.description)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        AniListApi.get150AllTimeAnimes { animeList ->
            if (animeList.isNotEmpty()) {
                adapter.updateList(animeList)
            } else {
                Log.d("anilist.co", "Nenhum anime retornado")
            }
        }

        val searchCamp = findViewById<EditText>(R.id.editTextPesquisa)
        val button  = findViewById<ImageButton>(R.id.imageButton)

        button.setOnClickListener {
            val queryText= searchCamp.text.toString()
            val intent = Intent(this, SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("Title", queryText)
            startActivity(intent)
        }

        searchCamp.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val queryText= searchCamp.text.toString()
                val intent = Intent(this, SearchActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("Title", queryText)
                startActivity(intent)

                true
            } else {
                false
            }
        }


        findViewById<LinearLayout>(R.id.btnAnimeList).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)

        }
        findViewById<LinearLayout>(R.id.btnMangaList).setOnClickListener {
            val intent = Intent(this, SuggestActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.btnPerfil).setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }


    }
}
