package com.telkom.admin

import android.content.Intent
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
import com.telkom.core.utils.SessionManager
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var testConnectionViewModel: TestConnection
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter

    lateinit var sessionManager: SessionManager

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

        sessionManager = SessionManager(this)

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
                        handleChatListSuccess(resource.data!!)
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

        // Observe refresh state
        lifecycleScope.launch {
            chatViewModel.isRefreshing.collect { isRefreshing ->
                binding.swipeRefreshLayout.isRefreshing = isRefreshing
            }
        }
    }

    private fun setupClickListeners() {
        binding.logoutButton.setOnClickListener {
            signOut()
            finish()
        }

        // Setup SwipeRefreshLayout
        binding.swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.primary)
            setOnRefreshListener {
                chatViewModel.refreshChats()
            }
        }
    }

    private fun hideAllStates() {
        binding.progressBar.isVisible = false
        binding.emptyState.isVisible = false
        binding.swipeRefreshLayout.isVisible = true
    }

    private fun showEmptyState() {
        binding.progressBar.isVisible = false
        binding.emptyState.isVisible = true
        binding.swipeRefreshLayout.isVisible = true
    }

    private fun showLoadingState() {
        binding.progressBar.isVisible = true
        binding.emptyState.isVisible = false
        binding.swipeRefreshLayout.isVisible = false
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


    private fun showErrorState(message: String) {
        hideAllStates()
//        binding.errorStateLayout.isVisible = true
//        binding.errorMessage.text = message
    }



    private fun onChatItemClick(chatItem: ChatItem) {
        // Ambil user_id dari sender (user yang chat ke admin)
        val targetUserId = chatItem.sender_id
//        val targetUserName = chatItem.sender_name ?: "User" // Jika ada field sender_name

        val intent = Intent().apply {
            setClassName(
                this@AdminActivity,
                "com.telkom.chat.ChatActivity"
            )
            putExtra("target_user_id", targetUserId)
//            putExtra("target_user_name", targetUserName)
        }
        startActivity(intent)
    }

    private fun signOut() {
        sessionManager.clearAuthToken()

        val intent = Intent().apply {
            setClassName(
                this@AdminActivity,
                "com.telkom.ceostar.ui.onboard.OnboardActivity" // Adjust this to your actual OnboardActivity class path
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

}