package com.telkom.ceostar.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ActivityConfirmationBinding
import com.telkom.ceostar.ui.home.HomeActivity
import com.telkom.core.utils.Resource
import com.telkom.core.utils.SessionManager
import com.telkom.core.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var email: String

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = 0

        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        email = intent.getStringExtra("EXTRA_EMAIL") ?: ""

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            authViewModel.loginState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.buttonLogin.isEnabled = false
                        binding.buttonLogin.text = "Loading..."
                    }

                    is Resource.Success -> {

                        val responseData = resource.data
                        val role = responseData?.user?.role ?: "user"
                        val token = responseData?.token
                        // Asumsi respons Anda memiliki field 'expires_in' dalam detik.
                        // Ganti 3600L dengan nilai default yang sesuai jika perlu.
                        val expiresIn = 604800L

                        if (token != null) {
                            // Simpan token beserta waktu kedaluwarsanya
                            sessionManager.saveAuthToken(token, expiresIn, role)
                        }

                        binding.buttonLogin.isEnabled = true
                        binding.buttonLogin.text = "MASUK"

                        if (role == "admin") {
                            try {
                                val intent = Intent().apply {
                                    setClassName(
                                        this@ConfirmationActivity,
                                        "com.telkom.admin.ui.AdminActivity"
                                    )
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                startActivity(intent)
                                finish()
                            } catch (e: Exception) {
                                // Fallback jika dynamic feature belum ter-install
                                Toast.makeText(this@ConfirmationActivity, "Admin module tidak tersedia", Toast.LENGTH_SHORT).show()
                                // Atau redirect ke HomeActivity sebagai fallback
                                val intent = Intent(this@ConfirmationActivity, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            // Redirect to UserActivity
                            val intent = Intent(this@ConfirmationActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

//                        // Redirect to MainActivity

                    }

                    is Resource.Error -> {
                        binding.buttonLogin.isEnabled = true
                        binding.buttonLogin.text = "MASUK"
                        Toast.makeText(
                            this@ConfirmationActivity,
                            "Login gagal: ${resource.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    null -> {
                        binding.buttonLogin.isEnabled = true
                        binding.buttonLogin.text = "MASUK"
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun performLogin() {
        val emailLogin = email
        val password = binding.passwordField.text.toString().trim()

        if (password.isEmpty()) {
            binding.passwordField.error = "Password tidak boleh kosong"
            return
        }

        // Clear all errors
        binding.passwordField.error = null

        // Reset previous state
        authViewModel.resetLoginState()

        // Call register
        authViewModel.login(
            email = emailLogin,
            password = password,
        )
    }

}