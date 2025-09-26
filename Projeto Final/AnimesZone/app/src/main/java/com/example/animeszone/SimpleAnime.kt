package com.example.animeszone

import android.accessibilityservice.GestureDescription

data class SimpleAnime(
    val titleRomaji: String,
    val coverImageUrl: String?,
    val bannerUrl: String?,
    val description: String,
    val id: Int
)
