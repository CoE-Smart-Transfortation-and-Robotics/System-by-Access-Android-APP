package com.telkom.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.telkom.admin.databinding.ActivityAdminBinding
import com.telkom.admin.recyclerview.ChatAdapter
import com.telkom.admin.viewmodel.AdminViewModelFactory
import com.telkom.admin.viewmodel.ChatViewModel
import com.telkom.admin.viewmodel.TestConnection
import com.telkom.ceostar.R
import com.telkom.core.data.model.ChatItem
import com.telkom.core.utils.Resource
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var testConnectionViewModel: TestConnection
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = 0

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = getColor(R.color.primary)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        setupViewModels()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Test connection first
        testConnectionViewModel.testConnection()
    }

    private fun setupViewModels() {
        val factory = AdminViewModelFactory(this)
        testConnectionViewModel = ViewModelProvider(this, factory)[TestConnection::class.java]
        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter { chatItem ->
            onChatItemClick(chatItem)
        }

        with(binding.recyclerViewChats) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@AdminActivity)
        }
    }

    private fun setupObservers() {
        // Observe connection state
        lifecycleScope.launch {
            testConnectionViewModel.connectionState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(this@AdminActivity, "Koneksi berhasil", Toast.LENGTH_SHORT).show()
                        // Load chat data after successful connection
                        chatViewModel.getAllChats()
                    }
                    is Resource.Error -> {
                        showErrorState(resource.message ?: "Koneksi gagal")
                    }
                    is Resource.Loading -> {
                        showLoadingState()
                    }
                    null -> {}
                }
            }
        }

        // Observe chat list state
        lifecycleScope.launch {
            chatViewModel.chatListState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
//                        handleChatListSuccess(resource.data)
                        Toast.makeText(this@AdminActivity, "Berhasil memuat chat", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(this@AdminActivity, resource.message ?: "Gagal memuat chat", Toast.LENGTH_LONG).show()
                        showErrorState(resource.message ?: "Gagal memuat chat")
                    }
                    is Resource.Loading -> {
                        showLoadingState()
                    }
                    null -> {}
                }
            }
        }
    }

    private fun setupClickListeners() {
//        binding.retryButton.setOnClickListener {
//            chatViewModel.getAllChats()
//        }

        binding.logoutButton.setOnClickListener {
            // Handle logout
            finish()
        }
    }

    private fun handleChatListSuccess(chatList: List<ChatItem>) {
        hideAllStates()

        if (chatList.isEmpty()) {
            showEmptyState()
        } else {
            binding.recyclerViewChats.isVisible = true
            chatAdapter.submitList(chatList)
        }
    }

    private fun showLoadingState() {
        hideAllStates()
        binding.progressBar.isVisible = true
    }

    private fun showErrorState(message: String) {
        hideAllStates()
//        binding.errorStateLayout.isVisible = true
//        binding.errorMessage.text = message
    }

    private fun showEmptyState() {
        hideAllStates()
//        binding.emptyStateLayout.isVisible = true
    }

    private fun hideAllStates() {
        binding.progressBar.isVisible = false
//        binding.errorStateLayout.isVisible = false
//        binding.emptyStateLayout.isVisible = false
        binding.recyclerViewChats.isVisible = false
    }

    private fun onChatItemClick(chatItem: ChatItem) {
        Toast.makeText(this, "Chat clicked: ${chatItem.message}", Toast.LENGTH_SHORT).show()
        // TODO: Navigate to chat detail
        // val intent = Intent(this, ChatDetailActivity::class.java)
        // intent.putExtra("chat_id", chatItem.id)
        // startActivity(intent)
    }

//    private fun observeConnection() {
//        lifecycleScope.launch {
//            viewModel.connectionState.collect { resource ->
//                when (resource) {
//                    is Resource.Success -> {
//                        // Connection successful, proceed with session check
////                        navigateToNextScreen()
//                        Toast.makeText(this@AdminActivity, "Berhasil", Toast.LENGTH_LONG).show()
//                    }
//
//                    is Resource.Error -> {
//                        // Connection failed, show error view
////                        showServerDownView()
//                        Toast.makeText(this@AdminActivity, resource.message, Toast.LENGTH_LONG).show()
//                    }
//
//                    is Resource.Loading -> {
//                        // Show loading state, splash screen is already visible
//                    }
//
//                    null -> {
//                        // Initial state
//                    }
//                }
//            }
//        }
//    }
}