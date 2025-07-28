package com.telkom.ceostar.ui.booking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.telkom.ceostar.R
import com.telkom.ceostar.core.data.model.BookingRequest
import com.telkom.ceostar.core.data.model.PassengerBooking
import com.telkom.ceostar.core.data.model.ScheduleData
import com.telkom.ceostar.core.data.model.Seat
import com.telkom.ceostar.core.viewmodel.SeatViewModel
import com.telkom.ceostar.databinding.ActivityChooseChairBinding
import com.telkom.ceostar.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.compareTo
import kotlin.text.get

@AndroidEntryPoint
class ChooseChairActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseChairBinding
    private val selectedSeats = mutableSetOf<String>()
    private var trainClass: String = "Ekonomi"
    private var maxPassengers: Int = 1
    private var trainData: ScheduleData? = null

    private val seatViewModel: SeatViewModel by viewModels()
    private var availableSeats: List<Seat> = emptyList()
    private var passengerList: List<PassengerData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseChairBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        trainData = intent.getParcelableExtra<ScheduleData>("EXTRA_DATA_SCHEDULE")
        trainClass = intent.getStringExtra("EXTRA_TRAIN_CLASS") ?: "Ekonomi"
        maxPassengers = intent.getIntExtra("EXTRA_PASSENGER", 1)

        passengerList = intent.getParcelableArrayListExtra<PassengerData>("EXTRA_PASSENGER_LIST") ?: emptyList()

        binding.departureDate.text = trainData?.timing?.scheduleDate + " ~ " + trainData?.timing?.departureTime?.substring(0, 5) + " - " + trainData?.timing?.arrivalTime?.substring(0, 5)

        setupUI()
//        generateSeats()
        observeViewModel()
        loadSeats()
        setupListeners()
    }

    private fun loadSeats() {
        trainData?.let { data ->
            val trainId = data.train.trainId
            val scheduleDate = data.timing.scheduleDate

            // Ambil origin dan destination station ID dari intent activity sebelumnya
            val originStationId = intent.getIntExtra("EXTRA_ORIGIN_ID", -1)
            val destinationStationId = intent.getIntExtra("EXTRA_DESTINATION_ID", -1)

            if (originStationId != -1 && destinationStationId != -1) {
                seatViewModel.getAvailableSeats(
                    trainId = trainId,
                    scheduleDate = scheduleDate,
                    originStationId = originStationId,
                    destinationStationId = destinationStationId
                )
            } else {
                android.widget.Toast.makeText(this, "Data stasiun tidak lengkap", android.widget.Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

//    private fun observeViewModel() {
//        seatViewModel.seats.observe(this) { seats ->
//            availableSeats = seats
//            generateSeats()
//        }
//
//        seatViewModel.isLoading.observe(this) { isLoading ->
//            if (isLoading) {
//                // Show loading
//                binding.progressBar.visibility = View.VISIBLE
//            } else {
//                binding.progressBar.visibility = View.GONE
//            }
//        }
//
//        seatViewModel.errorMessage.observe(this) { message ->
//            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun observeViewModel() {
        seatViewModel.seats.observe(this) { seats ->
            availableSeats = seats
            generateSeats()
        }

        seatViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        seatViewModel.errorMessage.observe(this) { message ->
            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
        }

        // Observer untuk hasil booking
        seatViewModel.bookingResult.observe(this) { result ->
            result?.let {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Booking berhasil!", Toast.LENGTH_SHORT).show()

                    // Kembali ke HomeActivity dan clear all activities
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Booking gagal: ${it.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupUI() {
        when (trainClass.uppercase()) {
            "EKONOMI" -> {
                binding.ekonomiLayout.visibility = View.VISIBLE
                binding.eksekutifLayout.visibility = View.GONE
            }
            "EKSEKUTIF" -> {
                binding.ekonomiLayout.visibility = View.GONE
                binding.eksekutifLayout.visibility = View.VISIBLE
            }
        }

        updateSelectedSeatsText()
    }

    private fun generateSeats() {
        when (trainClass.uppercase()) {
            "EKONOMI" -> generateEkonomiSeats()
            "EKSEKUTIF" -> generateEksekutifSeats()
        }
    }

    private fun generateEkonomiSeats() {
        val gridLayout = binding.ekonomiSeats
        gridLayout.removeAllViews()

        for (row in 1..18) {
            for (col in 1..4) {
                val seatNumber = "${row}${('A' + col - 1)}"
                val seatButton = createSeatButton(seatNumber)

                val params = GridLayout.LayoutParams().apply {
                    width = 100
                    height = 100
                    setMargins(4, 4, 4, 4)
                }
                seatButton.layoutParams = params

                gridLayout.addView(seatButton)
            }
        }
    }

    private fun generateEksekutifSeats() {
        val gridLayout = binding.eksekutifSeats
        gridLayout.removeAllViews()

        for (row in 1..13) {
            for (col in 1..4) {
                val seatNumber = "${row}${('A' + col - 1)}"

                // Create invisible view for seats 1D and 13A to maintain layout
                if (seatNumber == "1D" || seatNumber == "13A") {
                    val emptyView = View(this).apply {
                        val params = GridLayout.LayoutParams().apply {
                            width = 120
                            height = 120
                            setMargins(8, 8, 8, 8)
                        }
                        layoutParams = params
                        visibility = View.INVISIBLE
                    }
                    gridLayout.addView(emptyView)
                    continue
                }

                val seatButton = createSeatButton(seatNumber)

                val params = GridLayout.LayoutParams().apply {
                    width = 120
                    height = 120
                    setMargins(8, 8, 8, 8)
                }
                seatButton.layoutParams = params

                gridLayout.addView(seatButton)
            }
        }
    }

    private fun createSeatButton(seatNumber: String): Button {
        val button = Button(this).apply {
            text = seatNumber
            textSize = 10f
            background = ContextCompat.getDrawable(this@ChooseChairActivity, R.drawable.seat_available)
            setTextColor(ContextCompat.getColor(this@ChooseChairActivity, R.color.black))
        }

        // Check if seat is booked from API data - handle case conversion
        val seat = availableSeats.find {
            it.seat_number == seatNumber && it.`class`.equals(trainClass, ignoreCase = true)
        }

        if (seat != null && seat.is_booked == 1) {
            button.background = ContextCompat.getDrawable(this, R.drawable.seat_occupied)
            button.isEnabled = false
            button.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        button.setOnClickListener {
            toggleSeat(button, seatNumber)
        }

        return button
    }

    private fun toggleSeat(button: Button, seatNumber: String) {
        if (selectedSeats.contains(seatNumber)) {
            // Deselect seat
            selectedSeats.remove(seatNumber)
            button.background = ContextCompat.getDrawable(this, R.drawable.seat_available)
            button.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            // Check if max passengers reached
            if (selectedSeats.size >= maxPassengers) {
                android.widget.Toast.makeText(this, "Maksimal $maxPassengers kursi", android.widget.Toast.LENGTH_SHORT).show()
                return
            }

            // Select seat
            selectedSeats.add(seatNumber)
            button.background = ContextCompat.getDrawable(this, R.drawable.seat_selected)
            button.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        updateSelectedSeatsText()
        updateConfirmButton()
    }

    private fun updateConfirmButton() {
        // Enable button only when all passengers have seats
        binding.buttonConfirm.isEnabled = selectedSeats.isNotEmpty() && selectedSeats.size <= maxPassengers

        // Update button text to show progress
        val buttonText = if (selectedSeats.size == maxPassengers) {
            "KONFIRMASI KURSI"
        } else {
            "PILIH ${maxPassengers - selectedSeats.size} KURSI LAGI"
        }
        binding.buttonConfirm.text = buttonText
    }

    private fun updateSelectedSeatsText() {
        val text = if (selectedSeats.isEmpty()) {
            "Kursi Dipilih: -"
        } else {
            "Kursi Dipilih: ${selectedSeats.joinToString(", ")}"
        }
        binding.selectedSeatsText.text = text
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonConfirm.setOnClickListener {
            if (selectedSeats.size != maxPassengers) {
                Toast.makeText(this, "Silakan pilih ${maxPassengers} kursi sesuai jumlah penumpang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create booking
            createBooking()
        }
    }

    private fun createBooking() {
        val selectedSeatsList = selectedSeats.toList()
        val passengers = mutableListOf<PassengerBooking>()

        for (i in selectedSeatsList.indices) {
            if (i < passengerList.size) {
                // Find seat_id from availableSeats based on seat_number
                val seat = availableSeats.find {
                    it.seat_number == selectedSeatsList[i] &&
                            it.`class`.equals(trainClass, ignoreCase = true)
                }

                if (seat != null) {
                    passengers.add(
                        PassengerBooking(
                            seat_id = seat.seat_id,
                            name = passengerList[i].name,
                            nik = passengerList[i].nik
                        )
                    )
                }
            }
        }

        if (passengers.size == maxPassengers) {
            val bookingRequest = BookingRequest(
                train_id = trainData?.train?.trainId ?: 0,
                schedule_date = trainData?.timing?.scheduleDate ?: "",
                origin_station_id = intent.getIntExtra("EXTRA_ORIGIN_ID", -1),
                destination_station_id = intent.getIntExtra("EXTRA_DESTINATION_ID", -1),
                passengers = passengers
            )

            seatViewModel.createBooking(bookingRequest)



        } else {
            Toast.makeText(this, "Data penumpang tidak lengkap", Toast.LENGTH_SHORT).show()
        }
    }
}