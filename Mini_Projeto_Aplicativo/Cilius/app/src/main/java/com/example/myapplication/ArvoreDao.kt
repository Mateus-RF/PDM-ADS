package com.example.myapplication
import androidx.room.*

@Dao
interface ArvoreDao {
    @Insert
    suspend fun inserir(arvore: Arvore)

    @Update
    suspend fun atualizar(arvore: Arvore)

    @Delete
    suspend fun deletar(arvore: Arvore)

    @Query("SELECT * FROM arvores")
    suspend fun listarTodas(): List<Arvore>
}
