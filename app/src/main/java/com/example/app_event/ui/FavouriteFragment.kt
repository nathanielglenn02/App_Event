package com.example.app_event.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_event.databinding.FragmentFavouriteBinding
import com.example.app_event.model.EventItem
import com.example.app_event.repository.EventRepository
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: EventRepository
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = EventRepository(requireContext())
        binding.rvFavouriteEvents.layoutManager = LinearLayoutManager(requireContext())
        adapter = EventAdapter(emptyList(), this::onEventClick)
        binding.rvFavouriteEvents.adapter = adapter
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val favoriteEvents = repository.getAllFavoriteEvents()
            binding.progressBar.visibility = View.GONE

            if (favoriteEvents.isNotEmpty()) {
                adapter.updateEvents(favoriteEvents.map {
                    EventItem(
                        id = it.id.toString(),
                        name = it.name,
                        ownerName = it.ownerName,
                        beginTime = it.beginTime,
                        quota = it.quota,
                        registrants = it.registrants,
                        description = it.description,
                        imageLogo = it.imageLogo,
                        evenLink = it.evenLink
                    )
                })
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun onEventClick(event: EventItem) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
