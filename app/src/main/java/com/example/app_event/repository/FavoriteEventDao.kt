package com.example.app_event.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.app_event.model.FavoriteEvent

@Dao
interface FavoriteEventDao {

    // Menambahkan event ke dalam daftar favorit
    @Insert
    suspend fun addToFavorite(favoriteEvent: FavoriteEvent)

    // Menghapus event dari daftar favorit
    @Delete
    suspend fun removeFromFavorite(favoriteEvent: FavoriteEvent)

    // Mengambil semua event favorit
    @Query("SELECT * FROM favorite_events")
    suspend fun getFavoriteEvents(): List<FavoriteEvent>

    // Memeriksa apakah suatu event sudah ada di daftar favorit
    @Query("SELECT * FROM favorite_events WHERE id = :eventId")
    suspend fun isFavorite(eventId: Int): FavoriteEvent?
}
