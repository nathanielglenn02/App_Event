package com.example.app_event.ui

import EventViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_event.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Atur RecyclerView untuk event yang aktif
        binding.rvActiveEvents.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvActiveEvents.setHasFixedSize(true)

        // Atur RecyclerView untuk event yang sudah selesai
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvFinishedEvents.setHasFixedSize(true)

        // Tampilkan ProgressBar saat data dimuat
        binding.progressBar.visibility = View.VISIBLE

        // Muat event yang sedang aktif (5 event)
        viewModel.loadEvents(1)  // Status 1: Event yang akan datang

        // Observasi event aktif dan tampilkan di RecyclerView
        viewModel.events.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            if (events.isNotEmpty()) {
                val activeAdapter = EventAdapter(events.take(5))  // Ambil 5 event aktif pertama
                binding.rvActiveEvents.adapter = activeAdapter
            }
        }

        // Muat event yang sudah selesai (5 event)
        viewModel.loadEvents(0)  // Status 0: Event yang sudah selesai

        // Observasi event selesai dan tampilkan di RecyclerView
        viewModel.events.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            if (events.isNotEmpty()) {
                val finishedAdapter = EventAdapter(events.take(5))  // Ambil 5 event selesai pertama
                binding.rvFinishedEvents.adapter = finishedAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
