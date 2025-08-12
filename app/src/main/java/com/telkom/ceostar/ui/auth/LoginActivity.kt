package com.telkom.ceostar.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ActivityLoginBinding
import com.telkom.core.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = 0

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
//        observeLoginState()

//        binding.buttonLogin.setOnClickListener {
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
//        }
//
//        binding.buttonRegister.setOnClickListener {
//            // Navigate to RegisterActivity
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }

    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            goToConfirmation()
        }

        binding.buttonRegister.setOnClickListener {
            // Navigate to RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun goToConfirmation(){
        val email = binding.emailField.text.toString()

        if (email.isEmpty()) {
            binding.emailField.error = "Email tidak boleh kosong"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailField.error = "Format email tidak valid"
            return
        }

        binding.emailField.error = null // Clear any previous error

        val goToConfirmationActivity = Intent(this, ConfirmationActivity::class.java)
        goToConfirmationActivity.putExtra("EXTRA_EMAIL", email)
        startActivity(goToConfirmationActivity)
    }
}