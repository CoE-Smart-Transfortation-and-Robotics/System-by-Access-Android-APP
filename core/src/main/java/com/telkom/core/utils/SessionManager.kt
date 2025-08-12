package com.telkom.core.utils

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
        const val USER_ROLE = "user_role"
        const val USER_ID = "user_id"
        // 1. Key untuk waktu kedaluwarsa
        const val EXPIRATION_TIME = "expiration_time"
    }

    // 2. Modifikasi saveAuthToken
    fun saveAuthToken(token: String, expiresIn: Long, role: String = "user", id: Int) {
        // `expiresIn` adalah durasi dalam detik dari API
        val expirationTime = System.currentTimeMillis() + (expiresIn * 1000)
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_ROLE, role)
        editor.putInt(USER_ID, id)
        editor.putLong(EXPIRATION_TIME, expirationTime)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun fetchUserId(): Int? {
        return prefs.getInt(USER_ID, 0)
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
        editor.remove(USER_ROLE)
        editor.remove(USER_ID)
        editor.remove(EXPIRATION_TIME)
        editor.apply()
    }
}