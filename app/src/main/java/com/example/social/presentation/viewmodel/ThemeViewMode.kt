package com.example.social.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.domain.utils.ThemeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themeSettings = ThemeRepo(application)

    // Mutable state flow để lưu trạng thái giao diện sáng/tối
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    // Lấy trạng thái giao diện từ DataStore khi ViewModel được khởi tạo
    init {
        viewModelScope.launch {
            themeSettings.isDarkTheme.collect { savedTheme ->
                _isDarkTheme.value = savedTheme
            }
        }
    }

    // Hàm để lưu trạng thái sáng/tối vào DataStore khi người dùng thay đổi
    fun toggleTheme(isDark: Boolean) {
        // Kiểm tra nếu trạng thái không thay đổi thì không ghi
        if (_isDarkTheme.value != isDark) {
            viewModelScope.launch {
                themeSettings.saveTheme(isDark)
                _isDarkTheme.value = isDark
            }
        }
    }
}


