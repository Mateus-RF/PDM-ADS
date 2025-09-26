package com.example.animeszone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SimpleAnimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

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

        val titlePut = intent.getStringExtra("Title")
        if(!titlePut.isNullOrEmpty()){
            AniListApi.searchAnimesByTitle(
                title = titlePut,
                context = this,
                page = 1,
                perPage = 20
            ) { result->
                if (result.animes.isNotEmpty()) {
                    adapter.updateList(result.animes)
                } else {
                    AniListApi.getTop50Animes { animeList ->
                        if (animeList.isNotEmpty()) {
                            adapter.updateList(animeList)
                        } else {
                           Log.d("SearchActivity", "Nenhum anime retornado")
                        }
                    }
                }
            }
        }else {
            AniListApi.getTop50Animes { animeList ->
                if (animeList.isNotEmpty()) {
                    adapter.updateList(animeList)
                } else {
                    Log.d("SearchActivity", "Nenhum anime retornado")
                }
            }
        }


        val searchAnime = findViewById<EditText>(R.id.editTextSearch)
        val buttonSearch = findViewById<ImageButton>(R.id.imageButton)


        buttonSearch.setOnClickListener {
            val title = searchAnime.text.toString().trim()
            AniListApi.searchAnimesByTitle(
                title = title,
                context = this,
                page = 1,
                perPage = 20
            ) { result->
                if (result.animes.isNotEmpty()) {
                    adapter.updateList(result.animes)
                } else {
                    Log.d("AnimesSearch", "Nenhum anime retornado")
                }
            }
        }
        searchAnime.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val queryText = searchAnime.text.toString().trim()

                if (queryText.isNotEmpty()) {
                    AniListApi.searchAnimesByTitle(
                        title = queryText,
                        context = this,
                        page = 1,
                        perPage = 20
                    ) { result->
                        if (result.animes.isNotEmpty()) {
                            adapter.updateList(result.animes)
                        } else {
                            Log.d("SearchActivity", "Nenhum anime retornado")
                        }
                    }
                }
                true
            } else {
                false
            }
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        findViewById<LinearLayout>(R.id.btnHome).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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