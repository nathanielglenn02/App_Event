package com.example.app_event.network

import com.example.app_event.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("events")
    fun searchEvents(
        @Query("q") query: String
    ): Call<EventResponse>

    @GET("events")
    fun getActiveEvents(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1
    ): Call<EventResponse>
}
