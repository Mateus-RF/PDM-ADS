package com.example.animeszone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SimpleAnimeAdapter(
        private val animeList: MutableList<SimpleAnime>,
        private val onItemClick: (SimpleAnime) -> Unit
    ) :
    RecyclerView.Adapter<SimpleAnimeAdapter.AnimeViewHolder>() {

        inner class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageAnime: ImageView = itemView.findViewById(R.id.imageAnime)
            val textAnimeName: TextView = itemView.findViewById(R.id.textAnimeName)

            init {
                itemView.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClick(animeList[position])
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_anime_card, parent, false)
            return AnimeViewHolder(view)
        }

        override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
            val anime = animeList[position]
            holder.textAnimeName.text = anime.titleRomaji
            Glide.with(holder.itemView.context)
                .load(anime.coverImageUrl)
                .into(holder.imageAnime)
        }

        override fun getItemCount(): Int = animeList.size

        // 🔹 Aceitar lista de SimpleAnime
        fun updateList(newList: List<SimpleAnime>) {
            animeList.clear()              // agora funciona
            animeList.addAll(newList)      // adiciona os novos
            notifyDataSetChanged()
        }
}



