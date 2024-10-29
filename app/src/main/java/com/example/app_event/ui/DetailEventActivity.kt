package com.example.app_event.ui

import ViewModelFactory
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.app_event.R
import com.example.app_event.databinding.ActivityDetailEventBinding
import com.example.app_event.model.FavoriteEvent
import com.example.app_event.viewmodel.EventViewModel
import kotlinx.coroutines.launch

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var viewModel: EventViewModel
    private var isFavorite = false
    private var eventId = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory(applicationContext)
        viewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        eventId = intent.getIntExtra("event_id", 0) // Pastikan ID event diambil dari intent
        val eventName = intent.getStringExtra("event_name")
        val ownerName = intent.getStringExtra("owner_name")
        val beginTime = intent.getStringExtra("begin_time")
        val quota = intent.getIntExtra("quota", 0)
        val registrants = intent.getIntExtra("registrants", 0)
        val description = intent.getStringExtra("description")
        val imageLogo = intent.getStringExtra("image_logo")
        val eventLink = intent.getStringExtra("event_link")
        val remainingQuota = quota - registrants

        binding.tvEventName.text = eventName
        binding.tvOwnerName.text = ownerName
        binding.tvBeginTime.text = "Start: $beginTime"
        binding.tvQuota.text = "Quota: $remainingQuota"
        if (description != null) {
            binding.tvDescription.text = fromHtml(description)
        }

        Glide.with(this)
            .load(imageLogo)
            .into(binding.ivEventLogo)

        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))
            startActivity(intent)
        }

        lifecycleScope.launch {
            isFavorite = viewModel.isFavorite(eventId) // Periksa favorit berdasarkan ID event
            updateFavoriteButton()
        }

        binding.fabFavorite.setOnClickListener {
            lifecycleScope.launch {
                val favoriteEvent = FavoriteEvent(
                    id = eventId,
                    name = eventName ?: "",
                    ownerName = ownerName ?: "",
                    beginTime = beginTime ?: "",
                    quota = quota,
                    registrants = registrants,
                    description = description ?: "",
                    imageLogo = imageLogo ?: "",
                    evenLink = eventLink ?: ""
                )

                if (isFavorite) {
                    viewModel.removeEventFromFavorite(favoriteEvent)
                } else {
                    viewModel.addEventToFavorite(favoriteEvent)
                }
                isFavorite = !isFavorite
                updateFavoriteButton()
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    private fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favourite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favourite_border)
        }
    }

}
