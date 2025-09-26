package com.example.animeszone

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object GeminiApi {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private const val GEMINI_API_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"
    private const val API_KEY = "AIzaSyC2h_3YLIeug2LwrCbE0fhQkgpCjsDupd8"

    fun getSimilarAnimesViaGemini(
        context: Context,
        animeTitle: String,
        onComplete: (AnimeResult) -> Unit
    ) {
        val prompt = """
        Você é um recomendador de animes. 

        Sugira 10 títulos de animes similares a "$animeTitle".  
        Responda no formato exato abaixo:

        [
          {"title": "Fullmetal Alchemist: Brotherhood"},
          {"title": "Naruto"},
          ...
        ]

        **Observações importantes:**
        1. Sempre retorne 10 recomendações.
    """.trimIndent()

        val requestBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }

        val request = Request.Builder()
            .url("$GEMINI_API_URL?key=$API_KEY")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .header("Content-Type", "application/json")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val animeList = mutableListOf<SimpleAnime>()
            var rawResponse: String? = null

            try {
                val response = client.newCall(request).execute()
                rawResponse = response.body?.string()

                if (!rawResponse.isNullOrEmpty()) {
                    val jsonResponse = JSONObject(rawResponse)
                    val candidates = jsonResponse.optJSONArray("candidates")

                    if (candidates != null && candidates.length() > 0) {
                        val candidate = candidates.getJSONObject(0)
                        val content = candidate.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")

                        if (parts != null && parts.length() > 0) {
                            val textResponse = parts.getJSONObject(0).optString("text", "")

                            val jsonStart = textResponse.indexOf('[')
                            val jsonEnd = textResponse.lastIndexOf(']') + 1

                            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                                val jsonArrayStr = textResponse.substring(jsonStart, jsonEnd)
                                val jsonArray = JSONArray(jsonArrayStr)

                                for (i in 0 until jsonArray.length()) {
                                    val geminiAnime = jsonArray.getJSONObject(i)
                                    val title = geminiAnime.optString("title", "Unknown")

                                    val animeReal = try {
                                        val query = """
                                        query (${"$"}search: String, ${"$"}page: Int = 1, ${"$"}perPage: Int = 1) {
                                          Page(page: ${"$"}page, perPage: ${"$"}perPage) {
                                            media(search: ${"$"}search, type: ANIME) {
                                              id
                                              title { romaji }
                                              coverImage { large }
                                              bannerImage
                                              description
                                            }
                                          }
                                        }
                                    """.trimIndent()

                                        val variablesJson = JSONObject().apply { put("search", title) }
                                        val jsonBody = JSONObject().apply {
                                            put("query", query)
                                            put("variables", variablesJson)
                                        }

                                        val requestBody = jsonBody.toString()
                                            .toRequestBody("application/json; charset=utf-8".toMediaType())

                                        val aniRequest = Request.Builder()
                                            .url("https://graphql.anilist.co")
                                            .post(requestBody)
                                            .build()

                                        val aniResponse = client.newCall(aniRequest).execute()
                                        val aniJson = JSONObject(aniResponse.body?.string() ?: continue)
                                        val mediaArray = aniJson.getJSONObject("data")
                                            .getJSONObject("Page")
                                            .getJSONArray("media")
                                        if (mediaArray.length() == 0) continue

                                        val media = mediaArray.getJSONObject(0)
                                        val desc = media.optString("description", "").replace(Regex("<.*?>"), "")
                                        SimpleAnime(
                                            id = media.getInt("id"),
                                            titleRomaji = media.getJSONObject("title").getString("romaji"),
                                            coverImageUrl = media.getJSONObject("coverImage").getString("large"),
                                            bannerUrl = media.optString("bannerImage", null),
                                            description = desc
                                        )
                                    } catch (e: Exception) {
                                        Log.e("GeminiApi", "❌ Erro buscando na AniList: ${e.message}")
                                        null
                                    }

                                    animeReal?.let { animeList.add(it) }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GeminiApi", "❌ Erro na chamada Gemini: ${e.message}", e)
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete(AnimeResult(animeList, rawResponse))
                }
            }
        }
    }
}