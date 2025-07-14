package com.telkom.ceostar.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.telkom.ceostar.MainActivity
import com.telkom.ceostar.R
import com.telkom.ceostar.core.utils.Resource
import com.telkom.ceostar.core.viewmodel.AuthViewModel
import com.telkom.ceostar.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbarMain)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            authViewModel.registerState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.buttonRegister.isEnabled = false
                        binding.buttonRegister.text = "Loading..."
                    }
                    is Resource.Success -> {
                        binding.buttonRegister.isEnabled = true
                        binding.buttonRegister.text = "DAFTAR"
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

                        finish()
                    }
                    is Resource.Error -> {
                        binding.buttonRegister.isEnabled = true
                        binding.buttonRegister.text = "DAFTAR"
                        Toast.makeText(this@RegisterActivity, "Registrasi gagal: ${resource.message}", Toast.LENGTH_LONG).show()
                    }
                    null -> {
                        binding.buttonRegister.isEnabled = true
                        binding.buttonRegister.text = "DAFTAR"
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonRegister.setOnClickListener {
            performRegister()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun performRegister() {
        val name = binding.nameField.text.toString().trim()
        val email = binding.emailField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        val confirmPassword = binding.passwordConfirmationField.text.toString().trim()

        // Validation
        if (name.isEmpty()) {
            binding.nameField.error = "Nama tidak boleh kosong"
            return
        }

        if (email.isEmpty()) {
            binding.emailField.error = "Email tidak boleh kosong"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailField.error = "Format email tidak valid"
            return
        }

        if (password.isEmpty()) {
            binding.passwordField.error = "Password tidak boleh kosong"
            return
        }

        if (password.length < 6) {
            binding.passwordField.error = "Password minimal 6 karakter"
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.passwordConfirmationField.error = "Konfirmasi password tidak boleh kosong"
            return
        }

        if (password != confirmPassword) {
            binding.passwordConfirmationField.error = "Password tidak sama"
            return
        }

        // Clear all errors
        binding.nameField.error = null
        binding.emailField.error = null
        binding.passwordField.error = null
        binding.passwordConfirmationField.error = null

        // Reset previous state
        authViewModel.resetRegisterState()

        // Call register
        authViewModel.register(
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
    }

}