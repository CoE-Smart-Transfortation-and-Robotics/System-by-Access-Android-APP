package com.telkom.ceostar.ui.recylerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.databinding.ViewStationListBinding

class StationListAdapter(private var stationItems: List<StationList>) : RecyclerView.Adapter<StationListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewStationListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stationList: StationList) {
            binding.stationName.text = stationList.stationName
            binding.stationLocation.text = stationList.stationLocation
            binding.stationButton.setOnClickListener {
                stationList.onClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewStationListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stationItems[position])
    }

    override fun getItemCount(): Int {
        return stationItems.size
    }

    // Metode untuk memperbarui data dan memberi tahu adapter
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newStationItems: List<StationList>) {
        stationItems = newStationItems
        notifyDataSetChanged() // Memberi tahu RecyclerView untuk me-render ulang
    }
}