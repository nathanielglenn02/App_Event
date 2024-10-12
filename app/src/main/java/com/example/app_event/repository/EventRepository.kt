package com.example.app_event.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_event.model.EventItem
import com.example.app_event.model.EventResponse
import com.example.app_event.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository {

    // LiveData untuk menyimpan daftar event
    private val _events = MutableLiveData<List<EventItem>>()
    val events: LiveData<List<EventItem>> = _events

    // LiveData untuk status error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Fungsi untuk mengambil event dari API berdasarkan status (0 untuk selesai, 1 untuk akan datang)
    fun fetchEvents(status: Int) {
        val client = RetrofitClient.instance.getEvents(status)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _events.postValue(response.body()?.listEvents ?: listOf())
                }else{
                    _error.postValue("Error: ${response.message()}")  // Kode jika gagal mendapat data
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Tangani kesalahan saat gagal memuat data dari API
                _error.postValue("Failed to fetch data: ${t.message}")  // Error karena masalah jaringan atau lainnya
            }
        })
    }

    fun searchEvents(
        query: String,
        onSuccess: (List<EventItem>) -> Unit,
        onError: () -> Unit
    ) {
        val client = RetrofitClient.instance.searchEvents(query)  // Memanggil searchEvents dari ApiService
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()?.listEvents ?: listOf())  // Kirimkan hasil pencarian
                } else {
                    _events.postValue(listOf())  // Kosongkan jika gagal
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _events.postValue(listOf())  // Tangani error saat pencarian gagal
            }
        })
    }


}
