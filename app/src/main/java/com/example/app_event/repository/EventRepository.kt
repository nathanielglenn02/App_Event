package com.example.app_event.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_event.model.EventItem
import com.example.app_event.model.EventResponse
import com.example.app_event.model.FavoriteEvent
import com.example.app_event.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository(context: Context) {

    private val _events = MutableLiveData<List<EventItem>>()
    val events: LiveData<List<EventItem>> = _events
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val favoriteEventDao = AppDatabase.getDatabase(context).favoriteEventDao()

    fun fetchEvents(status: Int) {
        val client = RetrofitClient.instance.getEvents(status)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _events.postValue(response.body()?.listEvents ?: listOf())
                }else{
                    _error.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _error.postValue("Failed to fetch data: ${t.message}")
            }
        })
    }

    fun searchEvents(
        query: String,
        onSuccess: (List<EventItem>) -> Unit
    ) {
        val client = RetrofitClient.instance.searchEvents(query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()?.listEvents ?: listOf())
                } else {
                    _events.postValue(listOf())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _events.postValue(listOf())
            }
        })
    }

    suspend fun addEventToFavorite(event: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.addToFavorite(event)
        }
    }

    suspend fun removeEventFromFavorite(event: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.removeFromFavorite(event)
        }
    }

    suspend fun getAllFavoriteEvents(): List<FavoriteEvent> {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.getFavoriteEvents()
        }
    }

    suspend fun isFavorite(eventId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.isFavorite(eventId) != null
        }
    }


}
