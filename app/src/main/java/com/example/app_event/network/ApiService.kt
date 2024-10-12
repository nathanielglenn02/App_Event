package com.example.app_event.network

import com.example.app_event.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Mendapatkan daftar event berdasarkan status (0 = selesai, 1 = akan datang)
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>

    // Endpoint untuk mencari event berdasarkan kata kunci (query)
    @GET("events")
    fun searchEvents(
        @Query("q") query: String  // Query pencarian event
    ): Call<EventResponse>
}
