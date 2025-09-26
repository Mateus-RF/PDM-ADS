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

class SuggestActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SimpleAnimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_suggest)

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
            intent.putExtra("bannerUrl", animeSelecionado.bannerUrl)
            intent.putExtra("id", animeSelecionado.id.toString())
            intent.putExtra("description", animeSelecionado.description)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        val titlePut = findViewById<EditText>(R.id.editTextSuggest)
        val buttonSearch = findViewById<ImageButton>(R.id.buttonSearchByGemini)

        buttonSearch.setOnClickListener {
            val title = titlePut.text.toString().trim()

            adapter.updateList(emptyList())
            if (title.isNotEmpty()) {
                GeminiApi.getSimilarAnimesViaGemini(
                    context = this,
                    animeTitle = title
                ) { result ->
                    if (result.animes.isNotEmpty()) {
                        adapter.updateList(result.animes)
                    } else {
                        Log.d("AnimesSuggest", "Nenhum anime retornado pelo Gemini")
                    }
                }
            }
        }

        titlePut.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val queryText = titlePut.text.toString().trim()

                if (queryText.isNotEmpty()) {
                    GeminiApi.getSimilarAnimesViaGemini(
                        context = this,
                        animeTitle = queryText
                    ) { result ->
                        if (result.animes.isNotEmpty()) {
                            adapter.updateList(result.animes)
                        } else {
                            Log.d("AnimesSuggest", "Nenhum anime retornado pelo Gemini")
                        }
                    }
                }
                true
            } else {
                false
            }
        }
        findViewById<LinearLayout>(R.id.btnHome).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.btnAnimeList).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
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