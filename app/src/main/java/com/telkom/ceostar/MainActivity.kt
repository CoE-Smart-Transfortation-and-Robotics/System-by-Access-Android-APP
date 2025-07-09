package com.telkom.ceostar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.telkom.ceostar.core.utils.Resource
import com.telkom.ceostar.core.viewmodel.AuthViewModel
import com.telkom.ceostar.databinding.ActivityMainBinding
import com.telkom.ceostar.ui.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
        observeConnectionState() // <-- Aktifkan ini

        // Test connection saat app start
        authViewModel.testConnection()
    }

    private fun setupClickListeners() {
        binding.splashImage.setOnClickListener {
            Toast.makeText(this@MainActivity, "Testing connection...", Toast.LENGTH_SHORT).show()
            authViewModel.testConnection()
        }
    }

    private fun observeConnectionState() {
        lifecycleScope.launch {
            authViewModel.connectionState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Toast.makeText(this@MainActivity, "Mengecek koneksi...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(this@MainActivity, "✅ ${resource.data}", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(this@MainActivity, "❌ Koneksi gagal: ${resource.message}", Toast.LENGTH_LONG).show()
                    }
                    null -> {}
                }
            }
        }
    }
}