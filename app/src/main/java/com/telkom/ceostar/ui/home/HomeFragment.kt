package com.telkom.ceostar.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.telkom.ceostar.R
import com.telkom.ceostar.core.viewmodel.UserViewModel
import com.telkom.ceostar.databinding.FragmentHomeBinding
import com.telkom.ceostar.databinding.FragmentUserBinding
import com.telkom.ceostar.ui.user.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)

        observeProfile()
        viewModel.getProfile()
    }

    private fun observeProfile(){

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            android.util.Log.d("UserFragment", "Profile received: $profile")
            binding.namaUser.text = profile.name

        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            android.util.Log.d("UserFragment", "Loading state: $isLoading")
            if (!isLoading) {
                binding.namaUser.visibility = View.VISIBLE
            }
        }
    }

}