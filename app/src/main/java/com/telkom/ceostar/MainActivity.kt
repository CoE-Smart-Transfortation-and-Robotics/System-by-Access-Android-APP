package com.telkom.ceostar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            // Cek apakah token ada
            if (sessionManager.fetchAuthToken() != null) {
                // Jika ada, langsung ke HomeActivity
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                // Jika tidak ada, ke OnboardActivity
                startActivity(Intent(this, OnboardActivity::class.java))
            }
            finish() // Tutup MainActivity (splash screen)
        }, 3000) // 3 detik delay

    }

    private fun showServerDownView() {
        binding.splashImage.visibility = View.GONE
        val serverDown = findViewById<View>(R.id.server_down_view)
        serverDown.visibility = View.VISIBLE
    }


}