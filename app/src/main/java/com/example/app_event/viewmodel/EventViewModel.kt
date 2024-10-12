import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_event.model.EventItem
import com.example.app_event.repository.EventRepository

class EventViewModel : ViewModel() {

    private val repository = EventRepository()  // Inisialisasi repository

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData untuk event
    val events: LiveData<List<EventItem>> = repository.events  // Ambil data event dari repository

    // LiveData untuk hasil pencarian event
    private val _searchResults = MutableLiveData<List<EventItem>>()  // Tambahkan ini untuk hasil pencarian
    val searchResults: LiveData<List<EventItem>> = _searchResults

    // Fungsi untuk memuat event berdasarkan status
    fun loadEvents(status: Int) {
        _isLoading.value = true  // Tampilkan indikator loading
        repository.fetchEvents(status)  // Panggil fungsi di repository untuk ambil data

        // Sembunyikan indikator loading setelah data berhasil dimuat
        repository.events.observeForever {
            _isLoading.value = false
        }
    }

    // Fungsi untuk mencari event berdasarkan kata kunci
    fun searchEvents(query: String) {
        _isLoading.value = true

        repository.searchEvents(query, { resultList ->
            _searchResults.value = resultList  // Simpan hasil pencarian ke LiveData
            _isLoading.value = false
        }, {
            _searchResults.value = listOf()  // Kosongkan jika tidak ada hasil
            _isLoading.value = false
        })
    }
}

