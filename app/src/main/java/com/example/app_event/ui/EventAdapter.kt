package com.example.app_event.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_event.databinding.ItemEventBinding
import com.example.app_event.model.EventItem

class EventAdapter(private val events: List<EventItem>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventItem) {
            binding.tvEventName.text = event.name
            binding.tvOwnerName.text = event.ownerName

            // Memuat gambar event
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.ivEventLogo)

            // Saat item diklik, pindah ke DetailEventActivity
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra("event_name", event.name)
                intent.putExtra("owner_name", event.ownerName)
                intent.putExtra("begin_time", event.beginTime)
                intent.putExtra("quota", event.quota)
                intent.putExtra("registrants", event.registrants)
                intent.putExtra("description", event.description)
                intent.putExtra("image_logo", event.imageLogo)
                intent.putExtra("event_link", event.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}
