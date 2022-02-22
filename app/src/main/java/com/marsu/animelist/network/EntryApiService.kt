package com.marsu.animelist.network

import com.google.gson.JsonElement
import com.marsu.animelist.model.Entry
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.jikan.moe/v4/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface EntryApiService {
    @GET("anime")
    suspend fun getEntries(
        @Query("q") q : String,
        @Query("sfw") sfw : Boolean
    ) : JsonElement
}

object EntryApi {
    val retrofitService : EntryApiService by lazy {
        retrofit.create(EntryApiService::class.java)
    }
}