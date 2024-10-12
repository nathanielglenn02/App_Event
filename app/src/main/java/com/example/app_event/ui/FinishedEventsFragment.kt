package com.example.app_event.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_event.databinding.FragmentFinishedEventsBinding
import EventViewModel

class FinishedEventsFragment : Fragment() {

    private var _binding: FragmentFinishedEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Atur RecyclerView
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.setHasFixedSize(true)

        // Tampilkan ProgressBar saat data dimuat
        binding.progressBar.visibility = View.VISIBLE

        // Muat data untuk event yang sudah selesai
        viewModel.loadEvents(0)  // 0 untuk event yang sudah selesai

        // Observasi data event selesai
        viewModel.events.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE

            if (events.isNotEmpty()) {
                val adapter = EventAdapter(events)
                binding.rvFinishedEvents.adapter = adapter
            } else {
                // Tangani jika data kosong atau gagal dimuat
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Mencegah memory leaks
    }
}
