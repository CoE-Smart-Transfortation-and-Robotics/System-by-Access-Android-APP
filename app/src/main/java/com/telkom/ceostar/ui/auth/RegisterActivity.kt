package com.telkom.ceostar.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // Set status bar color to match app bar
        window.statusBarColor = getColor(R.color.primary)

        // Set status bar icons to light color (white)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Jangan set padding top agar tidak menutupi status bar
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbarMain)

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}