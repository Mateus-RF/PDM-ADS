package com.example.animeszone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {
    private lateinit var adapter: AnimePagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val titles = listOf("Completed", "Favorites", "Plan to Watch", "Watching")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        // Ajuste de padding para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        adapter = AnimePagerAdapter(this, titles)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        loadAllTabs()

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
        findViewById<LinearLayout>(R.id.btnMangaList).setOnClickListener {
            val intent = Intent(this, SuggestActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }
    private fun loadAllTabs() {
        titles.forEachIndexed { index, filter ->
            lifecycleScope.launch {
                val entities = loadData(filter)
                val simpleList = entities.map { entity ->
                    SimpleAnime(
                        titleRomaji = entity.titleRomaji,
                        coverImageUrl = entity.coverImageUrl,
                        bannerUrl = entity.bannerUrl,
                        description = entity.description.toString(),
                        id = entity.id
                    )
                }.toMutableList()

                withContext(Dispatchers.Main) {
                    adapter.setData(index, simpleList)
                }
            }
        }
    }

    private val repository = AnimeRepository()

    private suspend fun loadData(condition: String): MutableList<AnimeEntity> {
        return withContext(Dispatchers.IO) {
            val list = when (condition) {
                "Completed" -> repository.getCompleted()
                "Favorites" -> repository.getFavoriteAnimes()
                "Plan to Watch" -> repository.getPlanToWatch()
                "Watching" -> repository.getWatching()
                else -> emptyList()
            }
            list.toMutableList()
        }
    }

}
