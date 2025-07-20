package com.telkom.ceostar.ui.train

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.R
import com.telkom.ceostar.core.viewmodel.ScheduleViewModel
import com.telkom.ceostar.databinding.ActivityTrainScheduleBinding
import com.telkom.ceostar.databinding.ActivityTrainTypeBinding
import com.telkom.ceostar.ui.recylerview.TrainScheduleAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.getValue

@AndroidEntryPoint
class TrainScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainScheduleBinding
    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private lateinit var scheduleAdapter: TrainScheduleAdapter


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
        val originStationId = intent.getIntExtra("EXTRA_ORIGIN_ID", 0)
        val destinationStationId = intent.getIntExtra("EXTRA_DESTINATION_ID", 0)
        val trainType = intent.getStringExtra("EXTRA_TRAIN_TYPE")
        val adultCount = intent.getIntExtra("EXTRA_ADULT_COUNT", 0)


        binding.destinationText.text = destinationStation + " > " + originStation

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        if (originStationId != -1 && destinationStationId != -1 && departureDate != null) {

            val inputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Parse the date string to a LocalDate object
            val date = LocalDate.parse(departureDate, inputFormatter)

            // Format the LocalDate object to the desired string format
            val formattedDate = date.format(outputFormatter) // Result: "2025-07-20"

            scheduleViewModel.fetchTrainSchedules(originStationId, destinationStationId, formattedDate)
        } else {
            Toast.makeText(this, "Data tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        scheduleAdapter = TrainScheduleAdapter()
        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(this@TrainScheduleActivity)
            adapter = scheduleAdapter
        }
    }

    private fun observeViewModel() {
        scheduleViewModel.schedules.observe(this) { scheduleResponse ->
            if (scheduleResponse != null && scheduleResponse.data.isNotEmpty()) {
                scheduleAdapter.submitList(scheduleResponse.data)
            } else {
                // Handle empty or null data, maybe show a message
                Toast.makeText(this, "Jadwal tidak ditemukan", Toast.LENGTH_LONG).show()
            }
        }

        scheduleViewModel.isLoading.observe(this) { isLoading ->
            // Anda bisa menambahkan ProgressBar di layout dan menampilkannya di sini
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        }

        scheduleViewModel.errorMessage.observe(this) { errorMessage ->

            val emptySchedule = findViewById<View>(R.id.empty_schedules)
            emptySchedule.visibility = View.VISIBLE

            binding.rvSchedule.visibility = View.GONE

            if (errorMessage.isNotEmpty()) {

                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setupClickListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }

    }

}