package com.telkom.ceostar.ui.recylerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.core.data.model.ScheduleData
import com.telkom.ceostar.databinding.ViewTrainScheduleBinding
import java.text.NumberFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// Ubah constructor untuk menerima trainType
class TrainScheduleAdapter(private val trainType: String?) : ListAdapter<ScheduleData, TrainScheduleAdapter.ScheduleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ViewTrainScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = getItem(position)
        // Kirim trainType ke ViewHolder
        holder.bind(schedule, trainType)
    }

    class ScheduleViewHolder(private val binding: ViewTrainScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: ScheduleData, trainType: String?) {
            binding.apply {
                trainName.text = "${schedule.train.trainName} (${schedule.train.trainCode})"
                departureTime.text = schedule.timing.departureTime.substring(0, 5)
                arrivalTime.text = schedule.timing.arrivalTime.substring(0, 5)
                originStation.text = schedule.route.originStation
                destinationStation.text = schedule.route.destinationStation

                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val departure = LocalTime.parse(schedule.timing.departureTime, timeFormatter)
                val arrival = LocalTime.parse(schedule.timing.arrivalTime, timeFormatter)
                val duration = Duration.between(departure, arrival)

                val hours = duration.toHours()
                val minutes = duration.toMinutes() % 60

                val formattedDuration = String.format("%02dj %02dm", hours, minutes)
                totalTime.text = formattedDuration

                val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                format.maximumFractionDigits = 0

                // Menampilkan harga terendah sebagai default
                val lowestPrice = findLowestPrice(schedule)
                ticketPrice.text = format.format(lowestPrice)

                lookAllClass.setOnClickListener {
                    // Cek apakah salah satu kartu sudah terlihat untuk menentukan aksi (toggle)
                    val isExpanded = cardEconomy.visibility == View.VISIBLE ||
                            cardBusiness.visibility == View.VISIBLE ||
                            cardFirstClass.visibility == View.VISIBLE

                    if (isExpanded) {
                        // Jika sudah terlihat, sembunyikan semua kartu
                        cardEconomy.visibility = View.GONE
                        cardBusiness.visibility = View.GONE
                        cardFirstClass.visibility = View.GONE
                    } else {
                        // Jika tersembunyi, tampilkan kartu yang kursinya tersedia
                        if (schedule.seatClasses.ekonomi > 0) {
                            cardEconomy.visibility = View.VISIBLE
                            classTextEconomy.text = "EKONOMI"
                            classTextInsideEconomy.text = "EKONOMI"
                            ticketPriceEconomy.text = format.format(schedule.pricing.ekonomi)
                            economyAvailability.text = "Tersedia ${schedule.seatClasses.ekonomi}"
                        }
                        if (schedule.seatClasses.bisnis > 0) {
                            cardBusiness.visibility = View.VISIBLE
                            classTextBusiness.text = "BISNIS"
                            classTextInsideBusiness.text = "BISNIS"
                            ticketPriceBusiness.text = format.format(schedule.pricing.bisnis)
                            businessAvailability.text = "Tersedia ${schedule.seatClasses.bisnis}"
                        }
                        if (schedule.seatClasses.eksekutif > 0) {
                            cardFirstClass.visibility = View.VISIBLE
                            classTextFirstClass.text = "EKSEKUTIF"
                            classTextInsideFirstClass.text = "EKSEKUTIF"
                            // Perbaikan: Menggunakan ticketPriceFirstClass untuk harga eksekutif
                            ticketPriceFirstClass.text = format.format(schedule.pricing.eksekutif)
                            firstClassAvailability.text = "Tersedia ${schedule.seatClasses.eksekutif}"
                        }
                    }
                }
            }
        }

        private fun findLowestPrice(schedule: ScheduleData): Int {
            val prices = mutableListOf<Int>()
            if (schedule.seatClasses.ekonomi > 0) prices.add(schedule.pricing.ekonomi)
            if (schedule.seatClasses.bisnis > 0) prices.add(schedule.pricing.bisnis)
            if (schedule.seatClasses.eksekutif > 0) prices.add(schedule.pricing.eksekutif)
            return prices.minOrNull() ?: 0
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScheduleData>() {
            override fun areItemsTheSame(oldItem: ScheduleData, newItem: ScheduleData): Boolean {
                return oldItem.scheduleId == newItem.scheduleId
            }

            override fun areContentsTheSame(oldItem: ScheduleData, newItem: ScheduleData): Boolean {
                return oldItem == newItem
            }
        }
    }
}