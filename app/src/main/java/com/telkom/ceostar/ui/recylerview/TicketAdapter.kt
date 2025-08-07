package com.telkom.ceostar.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telkom.core.data.model.BookingTicket
import com.telkom.ceostar.databinding.ViewItemTicketBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TicketAdapter : ListAdapter<BookingTicket, TicketAdapter.TicketViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ViewItemTicketBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = getItem(position)
        holder.bind(ticket)
    }

    class TicketViewHolder(
        private val binding: ViewItemTicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: BookingTicket) {
            binding.apply {
                tvTrainName.text = "${ticket.trainSchedule.train.trainName} (${ticket.trainSchedule.train.trainCode})"
                tvStatus.text = ticket.status.uppercase()

                tvDepartureStation.text = ticket.originStation.stationName
                tvArrivalStation.text = ticket.destinationStation.stationName

                // Format tanggal
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                try {
                    val date = inputFormat.parse(ticket.trainSchedule.scheduleDate)
                    tvDate.text = outputFormat.format(date!!)
                } catch (e: Exception) {
                    tvDate.text = ticket.trainSchedule.scheduleDate
                }

                tvPassengerCount.text = "${ticket.passengers.size} Orang"

                // Format harga
                val price = ticket.price.toDoubleOrNull() ?: 0.0
                val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
//                tvPrice.text = formatter.format(price)

                // Status color
                when (ticket.status.lowercase()) {
                    "pending" -> {
                        tvStatus.setBackgroundResource(android.R.color.holo_orange_dark)
                    }
                    "confirmed" -> {
                        tvStatus.setBackgroundResource(android.R.color.holo_green_dark)
                    }
                    "cancelled" -> {
                        tvStatus.setBackgroundResource(android.R.color.holo_red_dark)
                    }
                }

                btnViewDetail.setOnClickListener {
                    // TODO: Navigate to detail ticket
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BookingTicket>() {
            override fun areItemsTheSame(oldItem: BookingTicket, newItem: BookingTicket): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BookingTicket, newItem: BookingTicket): Boolean {
                return oldItem == newItem
            }
        }
    }
}