package com.example.app_event.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_event.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventName = intent.getStringExtra("event_name")
        val ownerName = intent.getStringExtra("owner_name")
        val beginTime = intent.getStringExtra("begin_time")
        val quota = intent.getIntExtra("quota", 0)
        val registrants = intent.getIntExtra("registrants", 0)
        val description = intent.getStringExtra("description")
        val imageLogo = intent.getStringExtra("image_logo")
        val eventLink = intent.getStringExtra("event_link")
        val remainingQuota = quota-registrants

        binding.tvEventName.text = eventName
        binding.tvOwnerName.text = ownerName
        binding.tvBeginTime.text = "Start: $beginTime"
        binding.tvQuota.text = "Quota: $remainingQuota"
        binding.tvDescription.text = description

        Glide.with(this)
            .load(imageLogo)
            .into(binding.ivEventLogo)

        binding.btnOpenLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))
            startActivity(intent)
        }
    }
}
