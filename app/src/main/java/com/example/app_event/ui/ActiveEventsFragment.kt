package com.example.app_event.ui

import EventViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_event.databinding.FragmentActiveEventsBinding

class ActiveEventsFragment : Fragment() {

    private var _binding: FragmentActiveEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Atur RecyclerView
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.setHasFixedSize(true)

        // Tampilkan ProgressBar saat data dimuat
        binding.progressBar.visibility = View.VISIBLE

        // Muat data dari ViewModel
        viewModel.loadEvents(1)  // 1 untuk event yang akan datang
        

        // Observasi perubahan data event
        viewModel.events.observe(viewLifecycleOwner) { events ->
            // Sembunyikan ProgressBar saat data sudah dimuat
            binding.progressBar.visibility = View.GONE

            // Jika ada data event, tampilkan di RecyclerView
            if (events.isNotEmpty()) {
                val adapter = EventAdapter(events)
                binding.rvEvents.adapter = adapter
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
