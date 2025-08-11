package com.telkom.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.ceostar.R
import com.telkom.chat.databinding.ActivityChatBinding
import com.telkom.chat.recyclerview.ChatAdapter
import com.telkom.chat.viewmodel.ChatViewModel
import com.telkom.chat.viewmodel.ChatViewModelFactory
import com.telkom.chat.viewmodel.TestConnection
import com.telkom.core.utils.Resource
import com.telkom.core.utils.SessionManager
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var testConnectionViewModel: TestConnection
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var sessionManager: SessionManager

    companion object {
        const val EXTRA_TARGET_USER_ID = "target_user_id"
        const val EXTRA_TARGET_USER_NAME = "target_user_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = 0

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        // Gabungkan window insets handling untuk system bars DAN keyboard
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

            // Set padding untuk system bars (tetap seperti semula)
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)

            // Adjust bottom margin untuk input container saat keyboard muncul
            val bottomMargin = if (ime.bottom > 0) ime.bottom - systemBars.bottom else 0
            val layoutParams = binding.inputContainer.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = bottomMargin
            binding.inputContainer.layoutParams = layoutParams

            // Auto scroll ke bottom saat keyboard muncul
            if (ime.bottom > 0 && ::chatAdapter.isInitialized && chatAdapter.itemCount > 0) {
                binding.recyclerViewChat.post {
                    binding.recyclerViewChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }
            }

            insets
        }

        sessionManager = SessionManager(this)

        setupUI()
        setupViewModels()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Test connection first
        testConnectionViewModel.testConnection()
        chatViewModel.loadChatMessages()
    }

    private fun setupUI() {
        val targetUserName = intent.getStringExtra(EXTRA_TARGET_USER_NAME)
        val isAdmin = sessionManager.fetchUserRole()

        if (isAdmin == "admin" && targetUserName != null) {
            binding.toolbarTitle.text = "Chat dengan $targetUserName"
        } else {
            binding.toolbarTitle.text = "Chat dengan Admin"
        }
    }

    private fun setupViewModels() {
        val targetUserId = intent.getIntExtra(EXTRA_TARGET_USER_ID, -1)
        val finalTargetUserId = if (targetUserId != -1) targetUserId else null

        val factory = ChatViewModelFactory(this, finalTargetUserId)
        testConnectionViewModel = ViewModelProvider(this, factory)[TestConnection::class.java]
        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.recyclerViewChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            chatViewModel.messagesState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val messages = resource.data ?: emptyList()
                        if (messages.isEmpty()) {
                            binding.emptyState.visibility = View.VISIBLE
                            binding.recyclerViewChat.visibility = View.GONE
                        } else {
                            binding.emptyState.visibility = View.GONE
                            binding.recyclerViewChat.visibility = View.VISIBLE

                            val wasAtBottom = isRecyclerViewAtBottom()
                            chatAdapter.submitList(messages, chatViewModel.getCurrentUserId())

                            if (wasAtBottom || messages.isNotEmpty()) {
                                binding.recyclerViewChat.post {
                                    binding.recyclerViewChat.scrollToPosition(messages.size - 1)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ChatActivity, "DISINI ERROR", Toast.LENGTH_SHORT).show()
                        Log.e("INI ERROR YA", "Error loading messages: ${resource.message}")
                    }
                    null -> {}
                }
            }
        }

        lifecycleScope.launch {
            chatViewModel.sendMessageState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.buttonSend.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.buttonSend.isEnabled = true
                        binding.editTextMessage.setText("")
                        chatViewModel.clearSendMessageState()

                        binding.recyclerViewChat.post {
                            if (chatAdapter.itemCount > 0) {
                                binding.recyclerViewChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.buttonSend.isEnabled = true
                        Toast.makeText(this@ChatActivity, "Gagal mengirim pesan: ${resource.message}", Toast.LENGTH_SHORT).show()
                        chatViewModel.clearSendMessageState()
                    }
                    null -> {
                        binding.buttonSend.isEnabled = true
                    }
                }
            }
        }
    }

    private fun isRecyclerViewAtBottom(): Boolean {
        val layoutManager = binding.recyclerViewChat.layoutManager as? LinearLayoutManager
        return layoutManager?.let {
            val lastVisiblePosition = it.findLastCompletelyVisibleItemPosition()
            lastVisiblePosition >= chatAdapter.itemCount - 1
        } ?: true
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                chatViewModel.sendMessage(message)
            }
        }

        binding.editTextMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                binding.buttonSend.performClick()
                true
            } else {
                false
            }
        }
    }
}