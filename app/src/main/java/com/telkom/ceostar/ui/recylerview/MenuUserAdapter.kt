package com.telkom.ceostar.ui.recylerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telkom.ceostar.R
import com.telkom.ceostar.databinding.ViewButtonUserBinding

class MenuUserAdapter(private val userItems: List<MenuUser>) : RecyclerView.Adapter<MenuUserAdapter.ViewHolder>() {
    // ViewHolder berfungsi untuk menyimpan referensi view dari setiap item
    inner class ViewHolder(private val binding: ViewButtonUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuUser: MenuUser) {
            binding.btnMenuUser.text = menuUser.title
            binding.btnMenuUser.setCompoundDrawablesWithIntrinsicBounds(menuUser.iconLeft, 0, menuUser.iconRight, 0)
            // Anda bisa menambahkan OnClickListener di sini jika perlu
            binding.btnMenuUser.setOnClickListener {
                menuUser.onClick()
            }
        }
    }

    // Metode ini dipanggil untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewButtonUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // Metode ini untuk menghubungkan data dengan ViewHolder pada posisi tertentu
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userItems[position])
    }

    // Metode ini mengembalikan jumlah total item dalam daftar
    override fun getItemCount(): Int {
        return userItems.size
    }
}