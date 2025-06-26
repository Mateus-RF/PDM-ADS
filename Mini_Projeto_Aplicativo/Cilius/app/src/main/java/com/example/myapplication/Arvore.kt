package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arvores")
data class Arvore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val especie: String,
    val dataRegistro: String,
    val estadoConservacao: String,
    val latitude: Float,
    val longitude: Float,
    @ColumnInfo(name = "imagem_uri") val imagemUri: String? = null
)
