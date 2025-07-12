package com.telkom.ceostar.ui.train

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ActivityRegisterBinding
import com.telkom.ceostar.databinding.ActivityTrainTypeBinding
import com.telkom.ceostar.ui.onboard.OnboardActivity
import com.telkom.ceostar.ui.station.StationActivity

class TrainTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrainTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
    }

    private fun setupClickListeners(){

        binding.originButton.setOnClickListener {
            startActivity(Intent(this, StationActivity::class.java))
        }

    }
}