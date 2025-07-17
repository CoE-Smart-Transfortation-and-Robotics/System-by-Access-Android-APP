package com.telkom.ceostar.ui.train

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ActivityTrainScheduleBinding
import com.telkom.ceostar.databinding.ActivityTrainTypeBinding

class TrainScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrainScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val originStation =  intent.getStringExtra("EXTRA_ORIGIN")
        val destinationStation = intent.getStringExtra("EXTRA_DESTINATION")
        val departureDate = intent.getStringExtra("EXTRA_DATE")
        val trainType = intent.getStringExtra("EXTRA_TRAIN_TYPE")
        val adultCount = intent.getIntExtra("EXTRA_ADULT_COUNT", 0)


        binding.destinationText.text = destinationStation + " > " + originStation



        setupClickListeners()
    }


    private fun setupClickListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }

    }
            // Handle booking logic here
            // For now, just show a toast
}