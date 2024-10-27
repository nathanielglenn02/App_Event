package com.example.app_event.ui

import ViewModelFactory
import android.content.Intent
import com.example.app_event.viewmodel.EventViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_event.databinding.FragmentActiveEventsBinding

class ActiveEventsFragment : Fragment() {

    private var _binding: FragmentActiveEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EventViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[EventViewModel::class.java]

        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.setHasFixedSize(true)
        binding.progressBar.visibility = View.VISIBLE

        viewModel.loadEvents(1)
        viewModel.events.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            if (events.isNotEmpty()) {
                val adapter = EventAdapter(events) { event ->
                    val intent = Intent(requireContext(), DetailEventActivity::class.java)
                    intent.putExtra("event_name", event.name)
                    startActivity(intent)
                }
                binding.rvEvents.adapter = adapter
            } else {
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
