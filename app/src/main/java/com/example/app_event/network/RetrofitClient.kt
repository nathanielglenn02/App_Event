package com.example.app_event.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Base URL dari API
    private const val BASE_URL = "https://event-api.dicoding.dev/"

    // Membuat instance Retrofit hanya sekali (singleton pattern)
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Menggunakan Gson untuk parsing JSON
            .build()
    }

    // Inisialisasi ApiService dengan lazy
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
