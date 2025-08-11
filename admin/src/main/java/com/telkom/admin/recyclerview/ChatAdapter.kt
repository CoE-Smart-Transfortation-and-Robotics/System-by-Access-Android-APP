package com.telkom.admin.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.telkom.admin.databinding.ItemChatBinding
import com.telkom.core.data.model.ChatItem
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val onChatClick: (ChatItem) -> Unit
) : ListAdapter<ChatItem, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(
        private val binding: ItemChatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            with(binding) {
                // Set sender name based on role
                var urgent = "Not Urgent"
                if (chatItem.is_urgent) {
                    urgent = "Urgent"
                    root.setBackgroundColor(
                        itemView.context.getColor(com.telkom.ceostar.R.color.secondary)
                    )
                } else {
                    root.setBackgroundColor(
                        itemView.context.getColor(android.R.color.white)
                    )
                }
                senderNameTextView.text = when (chatItem.sender_role) {
                    "user" -> "User #${chatItem.sender_id} (${urgent})"
                    "admin" -> "Admin #${chatItem.sender_id}"
                    else -> "Unknown User"
                }

                // Set last message
                lastMessageTextView.text = chatItem.message

                // Format and set timestamp
                timestampTextView.text = formatTimestamp(chatItem.timestamp)

                // Set urgent indicator
                if (chatItem.is_urgent) {
                    unreadBadge.isVisible = true
                    unreadBadge.text = "!"
                    unreadBadge.setBackgroundColor(
                        itemView.context.getColor(com.telkom.ceostar.R.color.primary)
                    )
                } else {
                    unreadBadge.isVisible = false
                }

                // Set status icon (you can customize based on your needs)
//                statusIcon.isVisible = false

                // Set click listener
                root.setOnClickListener {
                    onChatClick(chatItem)
                }
            }
        }

        private fun formatTimestamp(timestamp: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = inputFormat.parse(timestamp)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                "00:00"
            }
        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatItem>() {
        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem == newItem
        }
    }
}