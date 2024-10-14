package com.example.app_event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_event.model.EventItem
import com.example.app_event.repository.EventRepository

class EventViewModel : ViewModel() {

    private val repository = EventRepository()
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
}

