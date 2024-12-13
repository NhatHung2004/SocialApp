package com.example.social.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
// Tạo DataStore
private val Context.dataStore by preferencesDataStore(name = "theme_preferences")

class ThemeRepo(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    // Lấy trạng thái sáng/tối từ DataStore
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
    }

    // Lưu trạng thái sáng/tối vào DataStore
    suspend fun saveTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}