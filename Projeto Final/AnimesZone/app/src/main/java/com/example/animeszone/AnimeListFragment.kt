package com.example.animeszone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnimeListFragment(private val animeList: MutableList<SimpleAnime>) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SimpleAnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_anime_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAnimes)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SimpleAnimeAdapter(animeList) { anime ->
            val intent = Intent(requireContext(), MediaAnime::class.java)
            intent.putExtra("id", anime.id.toString())
            intent.putExtra("title", anime.titleRomaji)
            intent.putExtra("coverUrl", anime.coverImageUrl)
            intent.putExtra("bannerUrl", anime.bannerUrl)
            intent.putExtra("description", anime.description)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        return view
    }
    fun updateData(newList: MutableList<SimpleAnime>) {
        animeList.clear()
        animeList.addAll(newList)
        if (this::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

}

