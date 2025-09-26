package com.example.animeszone

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

data class AnimeEntity(
    val id: Int = 0,
    val titleRomaji: String = "",
    val coverImageUrl: String? = null,
    val bannerUrl: String? = null,
    val description: String? = null,
    var status: String? = null,

    @get:PropertyName("favorite")
    @set:PropertyName("favorite")
    var isFavorite: Boolean = false
)
class AnimeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("animes")

    // Inserir ou atualizar
    suspend fun insertAnime(anime: AnimeEntity) {
        try {
            collection.document(anime.id.toString()).set(anime).await()
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao salvar anime", e)
        }
    }

    // Atualizar favorito
    suspend fun setFavorite(id: Int, fav: Boolean) {
        try {
            collection.document(id.toString()).update("favorite", fav).await()
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao atualizar favorito", e)
        }
    }
    // Buscar favorito por id
    suspend fun getFavoriteAnimeById(animeId: Int): Boolean {
        return try {
            val snapshot = collection.document(animeId.toString()).get().await()
            snapshot.get("favorite") == true
        } catch (e: Exception) {
            false
        }
    }
    // Buscar todos os favoritos
    suspend fun getFavoriteAnimes(): List<AnimeEntity> {
        return try {
            val snapshot = collection.whereEqualTo("favorite", true).get().await()
            snapshot.toObjects(AnimeEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
    // Buscar por status específico
    suspend fun getCompleted(): List<AnimeEntity> = getByStatus("Completed")
    suspend fun getPlanToWatch(): List<AnimeEntity> = getByStatus("Plan to Watch")
    suspend fun getWatching(): List<AnimeEntity> = getByStatus("Watching")
    private suspend fun getByStatus(status: String): List<AnimeEntity> {
        return try {
            val snapshot = collection.whereEqualTo("status", status).get().await()
            snapshot.toObjects(AnimeEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
    // Atualizar status
    suspend fun updateStatus(id: Int, status: String) {
        try {
            collection.document(id.toString()).update("status", status).await()
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao atualizar status", e)
        }
    }
    // Buscar por ID
    suspend fun getAnimeById(id: Int): AnimeEntity? {
        return try {
            val snapshot = collection.document(id.toString()).get().await()
            snapshot.toObject<AnimeEntity>()
        } catch (e: Exception) {
            null
        }
    }
    // Buscar status por ID
    suspend fun getStatusById(id: Int): String? {
        return try {
            val snapshot = collection.document(id.toString()).get().await()
            snapshot.toObject<AnimeEntity>()?.status
        } catch (e: Exception) {
            null
        }
    }
}