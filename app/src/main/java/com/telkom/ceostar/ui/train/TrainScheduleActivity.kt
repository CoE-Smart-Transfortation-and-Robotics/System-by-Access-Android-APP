package com.telkom.ceostar.ui.train

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.R
import com.telkom.ceostar.core.viewmodel.ScheduleViewModel
import com.telkom.ceostar.databinding.ActivityTrainScheduleBinding
import com.telkom.ceostar.ui.recylerview.TrainScheduleAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class TrainScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainScheduleBinding
    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private lateinit var scheduleAdapter: TrainScheduleAdapter
    private var trainType: String? = null

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

        val originStation = intent.getStringExtra("EXTRA_ORIGIN")
        val destinationStation = intent.getStringExtra("EXTRA_DESTINATION")
        val departureDate = intent.getStringExtra("EXTRA_DATE")
        val originStationId = intent.getIntExtra("EXTRA_ORIGIN_ID", -1)
        val destinationStationId = intent.getIntExtra("EXTRA_DESTINATION_ID", -1)
        val trainTypeId = intent.getIntExtra("EXTRA_TRAIN_ID", -1)

        binding.destinationText.text = "$originStation > $destinationStation"

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        if (originStationId != -1 && destinationStationId != -1 && !departureDate.isNullOrEmpty()) {
            val inputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(departureDate, inputFormatter)
            val formattedDate = date.format(outputFormatter)
            // Teruskan trainType ke ViewModel
            scheduleViewModel.fetchTrainSchedules(originStationId, destinationStationId, formattedDate, trainTypeId)
        } else {
            Toast.makeText(this, "Data tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerView() {
        // Perbaiki inisialisasi adapter dengan trainType
        scheduleAdapter = TrainScheduleAdapter(trainType)
        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(this@TrainScheduleActivity)
            adapter = scheduleAdapter
        }
    }

    private fun observeViewModel() {
        scheduleViewModel.schedules.observe(this) { scheduleResponse ->
            if (scheduleResponse != null && scheduleResponse.data.isNotEmpty()) {
                scheduleAdapter.submitList(scheduleResponse.data)
                binding.rvSchedule.visibility = View.VISIBLE
                binding.emptySchedules.visibility = View.GONE
            } else {
                binding.rvSchedule.visibility = View.GONE
                binding.emptySchedules.visibility = View.VISIBLE
                Toast.makeText(this, "Jadwal tidak ditemukan", Toast.LENGTH_LONG).show()
            }
        }

        scheduleViewModel.isLoading.observe(this) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        scheduleViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                binding.rvSchedule.visibility = View.GONE
                binding.emptySchedules.visibility = View.VISIBLE
//                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}