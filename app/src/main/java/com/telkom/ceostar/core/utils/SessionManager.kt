package com.telkom.ceostar.core.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    companion object {
        const val USER_TOKEN = "user_token"
        // 1. Key untuk waktu kedaluwarsa
        const val EXPIRATION_TIME = "expiration_time"
    }

    // 2. Modifikasi saveAuthToken
    fun saveAuthToken(token: String, expiresIn: Long) {
        // `expiresIn` adalah durasi dalam detik dari API
        val expirationTime = System.currentTimeMillis() + (expiresIn * 1000)
        editor.putString(USER_TOKEN, token)
        editor.putLong(EXPIRATION_TIME, expirationTime)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // 3. Fungsi untuk cek kedaluwarsa
    fun isTokenExpired(): Boolean {
        val expirationTime = prefs.getLong(EXPIRATION_TIME, 0)
        // Jika waktu kedaluwarsa adalah 0 (tidak pernah diset) atau sudah lewat, anggap kedaluwarsa
        return expirationTime == 0L || System.currentTimeMillis() >= expirationTime
    }

    // 4. Fungsi untuk menghapus token
    fun clearAuthToken() {
        editor.remove(USER_TOKEN)
        editor.remove(EXPIRATION_TIME)
        editor.apply()
    }
}