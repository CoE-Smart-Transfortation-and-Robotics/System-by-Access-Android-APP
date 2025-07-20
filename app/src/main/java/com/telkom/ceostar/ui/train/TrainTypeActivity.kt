package com.telkom.ceostar.ui.train

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.*
import com.telkom.ceostar.R
import com.telkom.ceostar.core.data.model.UserProfile
import com.telkom.ceostar.databinding.ActivityRegisterBinding
import com.telkom.ceostar.databinding.ActivityTrainTypeBinding
import com.telkom.ceostar.ui.onboard.OnboardActivity
import com.telkom.ceostar.ui.station.StationActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrainTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainTypeBinding

    private var trainType: String = ""
    private var trainId: Int = 0

    private var adultCount = 0

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

        val type = intent.getStringExtra("EXTRA_TYPE")
        trainType = type.toString()
        val typeId = intent.getIntExtra("EXTRA_TYPE_ID", 0)
        trainId = typeId

        binding.trainTypeText.text = trainType

        setupClickListeners()
        setupDatePicker()
    }

    private fun setupDatePicker() {
        // 1. Atur OnClickListener pada elemen UI Anda
        binding.departureDate.setOnClickListener {

            // ✨ Tambahan: Buat batasan tanggal (constraints)
            // Hanya izinkan tanggal dari hari ini ke depan
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.from(MaterialDatePicker.todayInUtcMilliseconds()))

            // 2. Buat builder untuk MaterialDatePicker
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Keberangkatan")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .setInputMode(INPUT_MODE_CALENDAR)
                .setTheme(R.style.MyDatePickerTheme)
                .build()


            // 4. Tampilkan Date Picker
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")

            // 5. Tangani hasil saat pengguna menekan tombol "OK"
            datePicker.addOnPositiveButtonClickListener { selection ->
                // 'selection' adalah tanggal yang dipilih dalam milidetik (Long)

                // Format tanggal menjadi string yang mudah dibaca
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = sdf.format(Date(selection))

                // 6. Tampilkan tanggal yang sudah diformat ke TextView
                binding.departureDateText.text = formattedDate
            }
        }
    }

    private fun setupClickListeners(){

        binding.originButton.setOnClickListener {
            openStationList("origin")
        }

        binding.destinationButton.setOnClickListener {
            openStationList("destination")
        }

        binding.passengerAmount.setOnClickListener {
            showPassengerDialog()
        }

        binding.buttonTicketSearch.setOnClickListener {
            val originStation = binding.originStation.text.toString()
            val destinationStation = binding.destinationStation.text.toString()
            val departureDate = binding.departureDateText.text.toString()
            val adultCount = this.adultCount

            if (originStation.isEmpty() || destinationStation.isEmpty() || departureDate.isEmpty()) {
                Toast.makeText(this, "Silakan lengkapi semua informasi", Toast.LENGTH_SHORT).show()
            } else {
                // Lanjutkan ke TrainScheduleActivity dengan data yang diperlukan
                val intent = Intent(this, TrainScheduleActivity::class.java)
                intent.putExtra("EXTRA_ORIGIN", originStation)
                intent.putExtra("EXTRA_DESTINATION", destinationStation)
                intent.putExtra("EXTRA_DATE", departureDate)
                intent.putExtra("EXTRA_TRAIN_TYPE", trainType)
                intent.putExtra("EXTRA_TRAIN_ID", trainId)
                intent.putExtra("EXTRA_ADULT_COUNT", adultCount)
                intent.putExtra("EXTRA_ORIGIN_ID", binding.originStationId.text.toString().toInt())
                intent.putExtra("EXTRA_DESTINATION_ID", binding.destinationStationId.text.toString().toInt())
                startActivity(intent)
            }
        }

    }

    private fun showPassengerDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.passenger_selector, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = builder.create()

        val tvAdultCount = dialogView.findViewById<TextView>(R.id.tv_adult_count)

        var tempAdultCount = adultCount

        tvAdultCount.text = tempAdultCount.toString()

        dialogView.findViewById<android.widget.ImageButton>(R.id.btn_adult_minus).setOnClickListener {
            if (tempAdultCount > 1) {
                tempAdultCount--
                tvAdultCount.text = tempAdultCount.toString()
            }
        }

        dialogView.findViewById<android.widget.ImageButton>(R.id.btn_adult_plus).setOnClickListener {
            tempAdultCount++
            tvAdultCount.text = tempAdultCount.toString()
        }

        builder.setPositiveButton("Pilih") { _, _ ->
            adultCount = tempAdultCount

            val passengerText = "$adultCount Dewasa"
            binding.passengerText.text = passengerText
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { _, _ ->
            dialog.dismiss()
        }

        builder.show()
    }



    private val stationSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 3. Tangani hasil yang kembali ke sini
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val stationName = data?.getStringExtra(StationActivity.EXTRA_STATION_NAME)
            val stationCode = data?.getStringExtra(StationActivity.EXTRA_STATION_CODE)
            val stationId = data?.getIntExtra(StationActivity.EXTRA_STATION_ID, 0)
            val type = data?.getStringExtra(StationActivity.EXTRA_TYPE)

            if (type.equals("origin")) {
                binding.originStation.text = stationName + " (" + stationCode + ")"
                binding.originStationId.text = stationId.toString()
            } else if (type.equals("destination")) {
                binding.destinationStation.text = stationName + " (" + stationCode + ")"
                binding.destinationStationId.text = stationId.toString()
            }
        }
    }

    // ... onCreate() atau di dalam fungsi lain ...

    private fun openStationList(type: String) {
        // 2. Gunakan launcher untuk memulai StationListActivity
        val intent = Intent(this, StationActivity::class.java)
        intent.putExtra(StationActivity.EXTRA_TYPE, type)
        stationSelectorLauncher.launch(intent)
    }

}