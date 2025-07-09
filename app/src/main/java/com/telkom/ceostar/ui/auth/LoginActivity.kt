package com.telkom.ceostar.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.telkom.ceostar.R
import com.telkom.ceostar.core.viewmodel.AuthViewModel
import com.telkom.ceostar.databinding.ActivityLoginBinding
import com.telkom.ceostar.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            val email = binding.emailField.text.toString()
        }

        binding.buttonRegister.setOnClickListener {
            // Navigate to RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}