package com.example.animeszone

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


data class AnimeResult(
    val animes: List<SimpleAnime>,
    val rawJson: String?)
object AniListApi {

    val client = OkHttpClient()
    fun searchAnimesByTitle(
        title: String,
        context: Context,
        page: Int = 1,
        perPage: Int = 10,
        onComplete: (AnimeResult) -> Unit
    ) {

        val query = """
                query (${"$"}search: String, ${"$"}page: Int, ${"$"}perPage: Int, ${"$"} isAdult: Boolean = false) {
                  Page(page: ${"$"}page, perPage: ${"$"}perPage) {
                    media(search: ${"$"}search, type: ANIME, isAdult: ${"$"}isAdult) {
                      id
                      title { romaji }
                      coverImage { large }
                      description
                      bannerImage
                    }
                  }
                }
                """.trimIndent()

        val variablesJson = JSONObject().apply {
            put("search", title)
            put("page", page)
            put("perPage", perPage)
        }

        val jsonBody = JSONObject().apply {
            put("query", query)
            put("variables", variablesJson)
        }

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://graphql.anilist.co")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            var resultJson: String? = null
            val animeList = mutableListOf<SimpleAnime>()

            try {
                val response = client.newCall(request).execute()
                resultJson = response.body?.string()

                if (!resultJson.isNullOrEmpty()) {
                    val json = JSONObject(resultJson)
                    val mediaArray = json.getJSONObject("data")
                        .getJSONObject("Page")
                        .getJSONArray("media")

                    for (i in 0 until mediaArray.length()) {
                        val media = mediaArray.getJSONObject(i)
                        val title = media.getJSONObject("title").optString("romaji", "Unknown")
                        val coverImageUrl = media.optJSONObject("coverImage")?.optString("large")
                        val bannerUrl = media.optString("bannerImage", null)
                        val description = media.optString("description", "").replace(Regex("<.*?>"), "")
                        val id = media.optInt("id")

                        animeList.add(
                            SimpleAnime(
                                titleRomaji = title,
                                coverImageUrl = coverImageUrl,
                                bannerUrl = bannerUrl,
                                description = description,
                                id = id
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete(AnimeResult(animeList, resultJson))
                }
            }
        }
    }

    fun getTop50Animes(onComplete: (List<SimpleAnime>) -> Unit) {

        val query = """
            query (${"$"}page: Int, ${"$"}perPage: Int, ${"$"} isAdult: Boolean = false) {
              Page(page: ${"$"}page, perPage: ${"$"}perPage) {
                media(type: ANIME, sort: SCORE_DESC, isAdult: ${"$"}isAdult) {
                  id
                  title { romaji }
                  coverImage { large }
                  bannerImage
                  description
                }
              }
            }
        """.trimIndent()

        val variables = JSONObject()
        variables.put("page", 1)
        variables.put("perPage", 50)

        val jsonBody = JSONObject()
        jsonBody.put("query", query)
        jsonBody.put("variables", variables)

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            jsonBody.toString()
        )

        val request = Request.Builder()
            .url("https://graphql.anilist.co")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val animeList = mutableListOf<SimpleAnime>()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!responseBody.isNullOrEmpty()) {
                    val json = JSONObject(responseBody)
                    val mediaArray = json.getJSONObject("data")
                        .getJSONObject("Page")
                        .getJSONArray("media")

                    for (i in 0 until mediaArray.length()) {
                        val media = mediaArray.getJSONObject(i)
                        val title = media.getJSONObject("title").optString("romaji", "Unknown")
                        val coverImageUrl = media.optJSONObject("coverImage")?.optString("large")
                        val bannerUrl = media.optString("bannerImage", null)
                        val description = media.optString("description", "").replace(Regex("<.*?>"), "")
                        val id = media.optInt("id")

                        animeList.add(
                            SimpleAnime(
                                titleRomaji = title,
                                coverImageUrl = coverImageUrl,
                                bannerUrl = bannerUrl,
                                description = description,
                                id = id
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete(animeList)
                }
            }
        }
    }

    fun get150AllTimeAnimes(onComplete: (List<SimpleAnime>) -> Unit) {
        val query = """
            query (${"$"}page: Int, ${"$"}perPage: Int, ${"$"}isAdult: Boolean = false) {
              Page(page: ${"$"}page, perPage: ${"$"}perPage) {
                media(type: ANIME, sort: POPULARITY_DESC, isAdult: ${"$"}isAdult) {
                  id
                  title { romaji }
                  coverImage { large }
                  bannerImage
                  description
                }
              }
            }
        """.trimIndent()

        val variablesJson = JSONObject().apply {
            put("page", 1)
            put("perPage", 50)
        }

        val jsonBody = JSONObject().apply {
            put("query", query)
            put("variables", variablesJson)
        }

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://graphql.anilist.co")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val animeList = mutableListOf<SimpleAnime>()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!responseBody.isNullOrEmpty()) {
                    val json = JSONObject(responseBody)
                    val mediaArray = json.getJSONObject("data")
                        .getJSONObject("Page")
                        .getJSONArray("media")

                    for (i in 0 until mediaArray.length()) {
                        val media = mediaArray.getJSONObject(i)
                        val title = media.getJSONObject("title").optString("romaji", "Unknown")
                        val coverImageUrl = media.optJSONObject("coverImage")?.optString("large")
                        val bannerUrl = media.optString("bannerImage", null)
                        val description = media.optString("description", "").replace(Regex("<.*?>"), "")
                        val id = media.optInt("id")

                        animeList.add(
                            SimpleAnime(
                                titleRomaji = title,
                                coverImageUrl = coverImageUrl,
                                bannerUrl = bannerUrl,
                                description = description,
                                id = id
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                withContext(Dispatchers.Main) {
                    onComplete(animeList)
                }
            }
        }
    }
}