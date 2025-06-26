package com.example.myapplication
import com.bumptech.glide.Glide
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArvoreAdapter(
    private val lista: List<Arvore>,
    private val onItemClick: (Arvore) -> Unit
) : RecyclerView.Adapter<ArvoreAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagem: ImageView = itemView.findViewById(R.id.imageViewArvore)
        private val texto: TextView = itemView.findViewById(R.id.textViewEspecie)

        fun bind(arvore: Arvore) {
            texto.text = arvore.especie

            if (!arvore.imagemUri.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(Uri.parse(arvore.imagemUri))
                    .placeholder(R.drawable.nature_icon)
                    .error(R.drawable.nature_icon)
                    .into(imagem)
            } else {
                imagem.setImageResource(R.drawable.nature_icon)
            }

            itemView.setOnClickListener {
                onItemClick(arvore)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_arvore, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }
}