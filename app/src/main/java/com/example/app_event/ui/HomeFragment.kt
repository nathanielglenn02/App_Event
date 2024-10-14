package com.example.app_event.ui

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        binding.rvActiveEvents.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvActiveEvents.setHasFixedSize(true)
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvFinishedEvents.setHasFixedSize(true)
        binding.progressBar.visibility = View.VISIBLE

        viewModel.loadEvents(1)
        viewModel.events.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE

            if (events.isNotEmpty()) {
                if (viewModel.getLastStatus() == 1) {
                    val activeAdapter = EventAdapter(events.take(5))
                    binding.rvActiveEvents.adapter = activeAdapter
                    viewModel.loadEvents(0)
                } else if (viewModel.getLastStatus() == 0) {
                    val finishedAdapter = EventAdapter(events.take(5))
                    binding.rvFinishedEvents.adapter = finishedAdapter
                }
            }
            else{
                viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
                    if (!errorMessage.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
