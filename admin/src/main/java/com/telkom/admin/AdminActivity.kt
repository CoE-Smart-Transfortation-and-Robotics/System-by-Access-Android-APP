package com.telkom.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.telkom.admin.databinding.ActivityAdminBinding
import com.telkom.admin.viewmodel.TestConnection
import com.telkom.admin.viewmodel.TestConnectionViewModelFactory
import com.telkom.core.utils.Resource
import kotlinx.coroutines.launch


class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var viewModel: TestConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = TestConnectionViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[TestConnection::class.java]

        observeConnection()
        viewModel.testConnection()
    }

    private fun observeConnection() {
        lifecycleScope.launch {
            viewModel.connectionState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        // Connection successful, proceed with session check
//                        navigateToNextScreen()
                        Toast.makeText(this@AdminActivity, "Berhasil", Toast.LENGTH_LONG).show()
                    }

                    is Resource.Error -> {
                        // Connection failed, show error view
//                        showServerDownView()
                        Toast.makeText(this@AdminActivity, resource.message, Toast.LENGTH_LONG).show()
                    }

                    is Resource.Loading -> {
                        // Show loading state, splash screen is already visible
                    }

                    null -> {
                        // Initial state
                    }
                }
            }
        }
    }
}