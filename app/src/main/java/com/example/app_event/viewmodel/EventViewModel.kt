package com.example.app_event.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_event.model.EventItem
import com.example.app_event.model.FavoriteEvent
import com.example.app_event.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventViewModel(context: Context) : ViewModel() {

    private val repository = EventRepository(context)
    private val _isLoading = MutableLiveData<Boolean>()

    val events: LiveData<List<EventItem>> = repository.events
    private val _searchResults = MutableLiveData<List<EventItem>>()
    val searchResults: LiveData<List<EventItem>> = _searchResults
    private var lastStatus: Int = 1

    val error: LiveData<String> = repository.error

    fun loadEvents(status: Int) {
        _isLoading.value = true
        lastStatus = status
        repository.fetchEvents(status)
    }


    fun searchEvents(query: String) {
        _isLoading.value = true

        repository.searchEvents(query) { resultList ->
            _searchResults.value = resultList
            _isLoading.value = false
        }
    }
    fun getLastStatus(): Int {
        return lastStatus
    }

    fun addEventToFavorite(event: FavoriteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEventToFavorite(event)
        }
    }

    fun removeEventFromFavorite(event: FavoriteEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeEventFromFavorite(event)
        }
    }

    suspend fun isFavorite(eventId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            repository.isFavorite(eventId)
        }
    }

}

