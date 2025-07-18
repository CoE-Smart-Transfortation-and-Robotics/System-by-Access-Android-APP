package com.telkom.ceostar.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.telkom.ceostar.core.data.model.User
import com.telkom.ceostar.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class PreferencesManager @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val TOKEN = stringPreferencesKey(Constants.TOKEN_KEY)
        val USER_ID = stringPreferencesKey(Constants.USER_ID_KEY)
        val USER_NAME = stringPreferencesKey(Constants.USER_NAME_KEY)
        val USER_EMAIL = stringPreferencesKey(Constants.USER_EMAIL_KEY)
    }

    suspend fun saveAuthData(token: String, user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = token
            preferences[PreferencesKeys.USER_ID] = user.id.toString()
            preferences[PreferencesKeys.USER_NAME] = user.name
            preferences[PreferencesKeys.USER_EMAIL] = user.email
        }
    }

    fun getAuthToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.TOKEN]
        }
    }

    fun getUserData(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val id = preferences[PreferencesKeys.USER_ID]?.toIntOrNull()
            val name = preferences[PreferencesKeys.USER_NAME]
            val email = preferences[PreferencesKeys.USER_EMAIL]

            if (id != null && name != null && email != null) {
                User(id = id, name = name, email = email)
            } else {
                null
            }
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.TOKEN)
            preferences.remove(PreferencesKeys.USER_ID)
            preferences.remove(PreferencesKeys.USER_NAME)
            preferences.remove(PreferencesKeys.USER_EMAIL)
        }
    }
}