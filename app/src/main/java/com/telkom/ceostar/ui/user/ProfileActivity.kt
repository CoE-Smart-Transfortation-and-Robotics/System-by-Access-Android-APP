package com.telkom.ceostar.ui.user

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
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.utils.Resource
import com.telkom.ceostar.core.viewmodel.UserViewModel
import com.telkom.ceostar.databinding.ActivityProfileBinding
import com.telkom.ceostar.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val userViewModel: UserViewModel by viewModels()

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val profile = intent.getParcelableExtra<UserProfile>("EXTRA_PROFILE")
        id = profile?.id ?: 0

        binding.nameField.setText(profile?.name)
        binding.emailField.setText(profile?.email)
        binding.phoneField.setText(profile?.phone)
        binding.addressField.setText(profile?.address)
        binding.nikField.setText(profile?.nik)

        setupClickListeners()
        setupObservers()
    }

    private fun setupObservers(){
        lifecycleScope.launch {
            userViewModel.updateState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.buttonUpdate.isEnabled = false
                        binding.buttonUpdate.text = "Loading..."
                    }
                    is Resource.Success -> {
                        binding.buttonUpdate.isEnabled = true
                        binding.buttonUpdate.text = "UPDATE"
                        Toast.makeText(this@ProfileActivity, "BERHASIL UPDATE DATA ANDA", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        binding.buttonUpdate.isEnabled = true
                        binding.buttonUpdate.text = "UPDATE"
                        // Handle error, show message to user
                    }
                    null -> {
                        binding.buttonUpdate.isEnabled = true
                        binding.buttonUpdate.text = "UPDATE"
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonUpdate.setOnClickListener {
            performUpdate()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun performUpdate() {
        val name = binding.nameField.text.toString().trim()
        val email = binding.emailField.text.toString().trim()
        val phone = binding.phoneField.text.toString().trim()
        val address = binding.addressField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        val confirmPassword = binding.passwordConfirmationField.text.toString().trim()
        val nik = binding.nikField.text.toString().trim()

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

        if (password.isNotEmpty()) {
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
        }

        binding.nameField.error = null
        binding.emailField.error = null
        binding.passwordField.error = null
        binding.passwordConfirmationField.error = null


        userViewModel.updateProfile(
            id = id,
            name = (if (name.isNotEmpty()) name else null).toString(),
            email = (if (email.isNotEmpty()) email else null).toString(),
            password = if (password.isEmpty()) null else password,
            confirmPassword = if (confirmPassword.isEmpty()) null else confirmPassword,
            nik = (if (nik.isNotEmpty()) nik else null).toString(),
            phone = (if (phone.isNotEmpty()) phone else null).toString(),
            address = (if (address.isNotEmpty()) address else null).toString()
        )
    }
}