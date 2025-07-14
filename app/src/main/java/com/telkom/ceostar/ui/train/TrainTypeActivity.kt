package com.telkom.ceostar.ui.train

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.datepicker.MaterialDatePicker
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

        binding.trainTypeText.text = trainType

        setupClickListeners()
        setupDatePicker()
    }

    private fun setupDatePicker() {
        // 1. Atur OnClickListener pada LinearLayout
        binding.departureDate.setOnClickListener {
            // 2. Buat builder untuk MaterialDatePicker
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Keberangkatan")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Set tanggal hari ini sebagai default
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

    }

    private val stationSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 3. Tangani hasil yang kembali ke sini
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val stationName = data?.getStringExtra(StationActivity.EXTRA_STATION_NAME)
            val stationCode = data?.getStringExtra(StationActivity.EXTRA_STATION_CODE)
            val type = data?.getStringExtra(StationActivity.EXTRA_TYPE)

            if (type.equals("origin")) {
                binding.originStation.text = stationName + " (" + stationCode + ")"
            } else if (type.equals("destination")) {
                binding.destinationStation.text = stationName + " (" + stationCode + ")"
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