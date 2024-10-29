package com.example.app_event.ui

import ViewModelFactory
import com.example.app_event.viewmodel.EventViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_event.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private var isActiveEventLoaded = false
    private var isFinishedEventLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory = ViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[EventViewModel::class.java]

        binding.rvActiveEvents.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvActiveEvents.setHasFixedSize(true)
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvFinishedEvents.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        loadActiveEvents()
    }

    private fun loadActiveEvents() {
        viewModel.loadEvents(1)
        viewModel.events.observe(viewLifecycleOwner) { events ->
            if (viewModel.getLastStatus() == 1) {
                binding.progressBar.visibility = View.GONE

                if (events.isNotEmpty()) {
                    val activeAdapter = EventAdapter(events.take(5))
                    binding.rvActiveEvents.adapter = activeAdapter
                    isActiveEventLoaded = true
                }
                loadFinishedEvents()
            }
        }
    }

    private fun loadFinishedEvents() {
        binding.progressBar.visibility = View.VISIBLE

        viewModel.loadEvents(0)
        viewModel.events.observe(viewLifecycleOwner) { events ->
            if (viewModel.getLastStatus() == 0) {
                if (events.isNotEmpty()) {
                    val finishedAdapter = EventAdapter(events.take(5))
                    binding.rvFinishedEvents.adapter = finishedAdapter
                    isFinishedEventLoaded = true
                }

                if (isActiveEventLoaded && isFinishedEventLoaded) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


