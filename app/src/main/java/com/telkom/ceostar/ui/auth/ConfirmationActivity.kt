package com.telkom.ceostar.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.telkom.ceostar.R
import com.telkom.ceostar.core.utils.Resource
import com.telkom.ceostar.core.utils.SessionManager
import com.telkom.ceostar.core.viewmodel.AuthViewModel
import com.telkom.ceostar.databinding.ActivityConfirmationBinding
import com.telkom.ceostar.databinding.ActivityLoginBinding
import com.telkom.ceostar.databinding.ActivityRegisterBinding
import com.telkom.ceostar.ui.auth.RegisterActivity
import com.telkom.ceostar.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var email: String

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        val token = responseData?.token
                        // Asumsi respons Anda memiliki field 'expires_in' dalam detik.
                        // Ganti 3600L dengan nilai default yang sesuai jika perlu.
                        val expiresIn = 604800L

                        if (token != null) {
                            // Simpan token beserta waktu kedaluwarsanya
                            sessionManager.saveAuthToken(token, expiresIn)
                        }

                        binding.buttonLogin.isEnabled = true
                        binding.buttonLogin.text = "MASUK"
                        Toast.makeText(this@ConfirmationActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                        // Redirect to MainActivity
                        val intent = Intent(this@ConfirmationActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    is Resource.Error -> {
                        binding.buttonLogin.isEnabled = true
                        binding.buttonLogin.text = "MASUK"
                        Toast.makeText(this@ConfirmationActivity, "Login gagal: ${resource.message}", Toast.LENGTH_LONG).show()
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