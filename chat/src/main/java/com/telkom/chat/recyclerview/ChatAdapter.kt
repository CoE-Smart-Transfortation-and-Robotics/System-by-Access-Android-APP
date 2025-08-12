package com.telkom.chat.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telkom.chat.databinding.ItemChatMessageBinding
import com.telkom.core.data.model.ChatMessage

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private var messageList = listOf<ChatMessage>()
    private var currentUserId: Int = 0

    fun submitList(newList: List<ChatMessage>, userId: Int) {
        messageList = newList
        currentUserId = userId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    inner class ChatViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            with(binding) {
                // Format timestamp dari ISO string
                val timestamp = formatMessageTimestamp(message.timestamp)

                if (message.sender_id == currentUserId) {
                    // Pesan yang dikirim user (sebelah kanan)
                    layoutSent.visibility = View.VISIBLE
                    layoutReceived.visibility = View.GONE
                    textSent.text = message.message
                    timestampSent.text = timestamp
                } else {
                    // Pesan yang diterima dari admin (sebelah kiri)
                    layoutReceived.visibility = View.VISIBLE
                    layoutSent.visibility = View.GONE
                    textReceived.text = message.message
                    timestampReceived.text = timestamp
                }
            }
        }

        private fun formatMessageTimestamp(timestamp: String): String {
            return try {
                val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
                isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")

                val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

                val date = isoFormat.parse(timestamp)
                timeFormat.format(date ?: java.util.Date())
            } catch (e: Exception) {
                java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            }
        }
    }
}