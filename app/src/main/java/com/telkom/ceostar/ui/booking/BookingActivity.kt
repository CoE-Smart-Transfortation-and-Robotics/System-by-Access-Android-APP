package com.telkom.ceostar.ui.booking

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.telkom.ceostar.MainActivity
import com.telkom.ceostar.R
import com.telkom.ceostar.core.data.model.ScheduleData
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.core.viewmodel.UserViewModel
import com.telkom.ceostar.databinding.ActivityBookingBinding
import com.telkom.ceostar.databinding.ActivityRegisterBinding
import com.telkom.ceostar.ui.recylerview.PassengerInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding

    private val viewModel: UserViewModel by viewModels()

    private val passengerList = mutableListOf<PassengerInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val trainClass = intent.getStringExtra("EXTRA_TRAIN_CLASS")
        val trainData = intent.getParcelableExtra<ScheduleData>("EXTRA_DATA_SCHEDULE")
        val totalPassenger = intent.getIntExtra("EXTRA_PASSENGER", 0)
        binding.originStation.text = trainData?.route?.originStation
        binding.destinationStation.text = trainData?.route?.destinationStation
        binding.departureDate.text = trainData?.timing?.scheduleDate + " ~ " + trainData?.timing?.departureTime?.substring(0, 5) + " - " + trainData?.timing?.arrivalTime?.substring(0, 5)
        binding.trainName.text = trainData?.train?.trainName + " (" + trainData?.train?.trainCode + ")"
        binding.selectedClass.text = trainClass


        initializePassengerList(totalPassenger)

        setupPassengerViews(totalPassenger)
        setupOnClickListeners()
        observeProfile()
        viewModel.getProfile()
    }

    private fun initializePassengerList(totalPassenger: Int) {
        passengerList.clear()
        repeat(totalPassenger) {
            passengerList.add(PassengerInfo())
        }
    }

    private fun setupPassengerViews(totalPassenger: Int) {
        when (totalPassenger) {
            1 -> {
                binding.passenger2.visibility = View.GONE
                binding.passenger3.visibility = View.GONE
                binding.passenger4.visibility = View.GONE
            }
            2 -> {
                binding.passenger3.visibility = View.GONE
                binding.passenger4.visibility = View.GONE
            }
            3 -> {
                binding.passenger4.visibility = View.GONE
            }
            else -> {
                // All passengers are visible
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.editButton1.setOnClickListener {
            showPassengerEditDialog(0)
        }

        binding.editButton2.setOnClickListener {
            showPassengerEditDialog(1)
        }

        binding.editButton3.setOnClickListener {
            showPassengerEditDialog(2)
        }

        binding.editButton4.setOnClickListener { // editButton4
            showPassengerEditDialog(3)
        }

        binding.buttonBooking.setOnClickListener {
            // Validasi semua passenger sudah diisi
            if (validateAllPassengers()) {
                val intent = Intent(this, MainActivity::class.java)

                // Kirim data passenger sebagai ArrayList
                intent.putParcelableArrayListExtra("EXTRA_PASSENGER_LIST", ArrayList(passengerList))

                // Kirim data lain yang diperlukan
                val trainClass = intent.getStringExtra("EXTRA_TRAIN_CLASS")
                val trainData = intent.getParcelableExtra<ScheduleData>("EXTRA_DATA_SCHEDULE")
                val totalPassenger = intent.getIntExtra("EXTRA_PASSENGER", 0)

                intent.putExtra("EXTRA_TRAIN_CLASS", trainClass)
                intent.putExtra("EXTRA_DATA_SCHEDULE", trainData)
                intent.putExtra("EXTRA_PASSENGER", totalPassenger)

                startActivity(intent)
            } else {
                // Tampilkan pesan error
                android.widget.Toast.makeText(this, "Mohon lengkapi data semua penumpang", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateAllPassengers(): Boolean {
        val totalPassenger = intent.getIntExtra("EXTRA_PASSENGER", 0)

        for (i in 0 until totalPassenger) {
            if (i < passengerList.size) {
                val passenger = passengerList[i]
                if (passenger.nik.isEmpty() || passenger.name.isEmpty()) {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }

    private fun showPassengerEditDialog(passengerIndex: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_passenger_edit, null)
        val etNik = dialogView.findViewById<EditText>(R.id.et_nik)
        val etName = dialogView.findViewById<EditText>(R.id.et_name)

        // Pre-fill existing data if available
        if (passengerIndex < passengerList.size) {
            etNik.setText(passengerList[passengerIndex].nik)
            etName.setText(passengerList[passengerIndex].name)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Penumpang ${passengerIndex + 1}")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val nik = etNik.text.toString().trim()
                val name = etName.text.toString().trim()

                if (nik.isNotEmpty() && name.isNotEmpty()) {
                    // Save passenger data
                    if (passengerIndex < passengerList.size) {
                        passengerList[passengerIndex].nik = nik
                        passengerList[passengerIndex].name = name
                    }

                    // Update UI
                    updatePassengerNameDisplay(passengerIndex, name)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun updatePassengerNameDisplay(passengerIndex: Int, name: String) {
        when (passengerIndex) {
            0 -> binding.passengerName1.text = name
            1 -> binding.passengerName2.text = name
            2 -> binding.passengerName3.text = name
            3 -> binding.passengerName4.text = name
        }
    }

    private fun observeProfile(){

        viewModel.profile.observe(this) { profile ->
            binding.passengerName.text = profile.name
            binding.passengerEmail.text = profile.email

            if (profile.phone.isNullOrEmpty()) {
                binding.passengerPhone.text = "Not Provided"
            }
            else {
                binding.passengerPhone.text = profile.phone
            }

        }

        viewModel.isLoading.observe(this) { isLoading ->
            android.util.Log.d("UserFragment", "Loading state: $isLoading")
            if (!isLoading) {
                binding.progressBar.visibility = View.GONE
                binding.layoutBooking.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.layoutBooking.visibility = View.GONE
            }
        }
    }
}