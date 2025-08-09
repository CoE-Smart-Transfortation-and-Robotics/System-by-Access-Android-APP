package com.telkom.admin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telkom.core.data.local.PreferencesManager
import com.telkom.core.data.repository.AuthRepository
import com.telkom.core.di.NetworkModule

class TestConnectionViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestConnection::class.java)) {
            // Gunakan method dari NetworkModule
            val apiService = NetworkModule.createApiService()
            val preferencesManager = PreferencesManager(context)
            val authRepository = AuthRepository(apiService, preferencesManager)

            return TestConnection(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}