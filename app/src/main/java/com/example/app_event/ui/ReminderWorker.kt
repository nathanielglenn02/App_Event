package com.example.app_event.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.app_event.R
import com.example.app_event.model.EventResponse
import com.example.app_event.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReminderWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        fetchAndNotifyEvent()
        return Result.success()
    }

    private fun fetchAndNotifyEvent() {
        val call = RetrofitClient.instance.getActiveEvents()
        call.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    if (!events.isNullOrEmpty()) {
                        val event = events[0]
                        showNotification(event.name, event.beginTime)
                    }
                }
            }
            @SuppressLint("MissingPermission")
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Menampilkan log error
                Log.e("ReminderWorker", "Failed to fetch active event: ${t.message}")

                // Menampilkan notifikasi yang menyatakan bahwa pengambilan data event gagal
                val channelId = "daily_reminder_channel"
                val channelName = "Daily Reminder"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    val manager = context.getSystemService(NotificationManager::class.java)
                    manager.createNotificationChannel(channel)
                }

                val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle("Failed to fetch event")
                    .setContentText("Gagal mengambil event aktif terdekat, periksa koneksi internet Anda.")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                NotificationManagerCompat.from(context).notify(1, notification)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(eventName: String, eventDate: String) {
        val channelId = "daily_reminder_channel"
        val channelName = "Daily Reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Event Terdekat")
            .setContentText("Event: $eventName pada $eventDate")
            .setSmallIcon(R.drawable.ic_notifications)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
