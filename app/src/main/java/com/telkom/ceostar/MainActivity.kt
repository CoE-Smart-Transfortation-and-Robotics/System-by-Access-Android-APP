package com.telkom.ceostar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.telkom.ceostar.core.utils.Resource
import com.telkom.ceostar.core.utils.SessionManager
import com.telkom.ceostar.core.viewmodel.AuthViewModel
import com.telkom.ceostar.databinding.ActivityMainBinding
import com.telkom.ceostar.ui.home.HomeActivity
import com.telkom.ceostar.ui.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sessionManager: SessionManager

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeConnection()
        authViewModel.testConnection()
    }

    private fun observeConnection() {
        lifecycleScope.launch {
            authViewModel.connectionState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        // Connection successful, proceed with session check
                        navigateToNextScreen()
                    }

                    is Resource.Error -> {
                        // Connection failed, show error view
                        showServerDownView()
                        Toast.makeText(this@MainActivity, resource.message, Toast.LENGTH_LONG).show()
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

    private fun navigateToNextScreen() {
        // Cek apakah token ada
        if (sessionManager.fetchAuthToken() != null) {
            // Jika ada, langsung ke HomeActivity
            if (sessionManager.fetchUserRole() == "admin") {
                try {
                    val intent = Intent().apply {
                        setClassName(
                            this@MainActivity,
                            "com.telkom.admin.ui.AdminActivity"
                        )
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    // Fallback jika dynamic feature belum ter-install
                    Toast.makeText(this@MainActivity, "Admin module tidak tersedia", Toast.LENGTH_SHORT).show()
                    // Atau redirect ke HomeActivity sebagai fallback
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            } else {
                // Jika bukan admin, ke OnboardActivity
                startActivity(Intent(this, HomeActivity::class.java))
            }
        } else {
            // Jika tidak ada, ke OnboardActivity
            startActivity(Intent(this, OnboardActivity::class.java))
        }
        finish()
    }

    private fun showServerDownView() {
        binding.splashImage.visibility = View.GONE
        val serverDown = findViewById<View>(R.id.server_down_view)
        serverDown.visibility = View.VISIBLE
    }
}