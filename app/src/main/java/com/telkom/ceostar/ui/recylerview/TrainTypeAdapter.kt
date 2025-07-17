package com.telkom.ceostar.ui.recylerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ViewButtonUserBinding
import com.telkom.ceostar.databinding.ViewTrainTypeListBinding

class TrainTypeAdapter(private val trainItems: List<TrainType>) : RecyclerView.Adapter<TrainTypeAdapter.ViewHolder>() {
    // ViewHolder berfungsi untuk menyimpan referensi view dari setiap item
    inner class ViewHolder(private val binding: ViewTrainTypeListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trainType: TrainType) {
            binding.trainType.text = trainType.title
            binding.trainImage.setImageResource(trainType.trainImage)
            binding.trainCard.setOnClickListener {
                trainType.onClick()
            }
        }
    }

    // Metode ini dipanggil untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewTrainTypeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // Metode ini untuk menghubungkan data dengan ViewHolder pada posisi tertentu
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trainItems[position])
    }

    // Metode ini mengembalikan jumlah total item dalam daftar
    override fun getItemCount(): Int {
        return trainItems.size
    }
}