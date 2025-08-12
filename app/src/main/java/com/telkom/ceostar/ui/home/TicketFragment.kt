package com.telkom.ceostar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.databinding.FragmentTicketBinding
import com.telkom.ceostar.ui.recylerview.TicketAdapter
import com.telkom.core.utils.Resource
import com.telkom.core.viewmodel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!

    private val ticketViewModel: TicketViewModel by viewModels()
    private lateinit var ticketAdapter: TicketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTicketBinding.bind(view)

        setupRecyclerView()
        observeTickets()
        loadTickets()
    }

    private fun setupRecyclerView() {
        ticketAdapter = TicketAdapter()
        binding.rvTickets.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeTickets() {
        viewLifecycleOwner.lifecycleScope.launch {
            ticketViewModel.ticketsState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showEmptyState(false)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        val tickets = resource.data ?: emptyList()
                        if (tickets.isEmpty()) {
                            showEmptyState(true)
                        } else {
                            showEmptyState(false)
                            ticketAdapter.submitList(tickets)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showEmptyState(true)
                        // TODO: Show error message
                    }
                }
            }
        }
    }

    private fun loadTickets() {
        ticketViewModel.loadMyTickets()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}