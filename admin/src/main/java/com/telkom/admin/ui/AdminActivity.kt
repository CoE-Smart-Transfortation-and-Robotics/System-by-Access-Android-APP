package com.telkom.admin.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.telkom.admin.R
import com.telkom.admin.databinding.ActivityAdminBinding
import com.telkom.core.utils.Resource
import com.telkom.core.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

//    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = 0

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        observePingResult()
    }

    private fun observePingResult() {
//        lifecycleScope.launch {
//            authViewModel.connectionState.collect { resource ->
//                when (resource) {
//                    is Resource.Success -> {
//                        // Connection successful, proceed with session check
//                        Toast.makeText(this@AdminActivity, "Sukses", Toast.LENGTH_LONG).show()
//                    }
//
//                    is Resource.Error -> {
//                        // Connection failed, show error view
//                        Toast.makeText(this@AdminActivity, "Gagal", Toast.LENGTH_LONG).show()
//                    }
//
//                    is Resource.Loading -> {
//                        // Show loading state, splash screen is already visible
//                        Toast.makeText(this@AdminActivity, resource.message, Toast.LENGTH_LONG).show()
//                    }
//
//                    null -> {
//                        // Initial state
//                    }
//                }
//            }
//        }
    }
}