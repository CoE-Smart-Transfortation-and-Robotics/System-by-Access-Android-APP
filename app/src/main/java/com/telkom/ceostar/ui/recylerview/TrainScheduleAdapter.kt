package com.telkom.ceostar.ui.recylerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.core.data.model.ScheduleData
import com.telkom.ceostar.databinding.ViewTrainScheduleBinding
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TrainScheduleAdapter : ListAdapter<ScheduleData, TrainScheduleAdapter.ScheduleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ViewTrainScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = getItem(position)
        holder.bind(schedule)
    }

    class ScheduleViewHolder(private val binding: ViewTrainScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: ScheduleData) {
            binding.apply {
                trainName.text = "${schedule.train.trainName} (${schedule.train.trainCode})"
//                tvTrainCategory.text = schedule.train.category
                departureTime.text = schedule.timing.departureTime.substring(0, 5) // HH:mm
                arrivalTime.text = schedule.timing.arrivalTime.substring(0, 5) // HH:mm
                originStation.text = schedule.route.originStation
                destinationStation.text = schedule.route.destinationStation

                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val departure = LocalTime.parse(schedule.timing.departureTime, timeFormatter)
                val arrival = LocalTime.parse(schedule.timing.arrivalTime, timeFormatter)
                val duration = Duration.between(departure, arrival)

                val hours = duration.toHours()
                val minutes = duration.toMinutes() % 60

                // Format to "03j 15m"
                val formattedDuration = String.format("%02dj %02dm", hours, minutes)
                totalTime.text = formattedDuration
            }
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