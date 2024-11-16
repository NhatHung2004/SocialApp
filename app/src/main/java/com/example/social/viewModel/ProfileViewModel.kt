package com.example.social.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _imageAvatarUri = MutableStateFlow<Uri?>(null)
    val imageAvatarUri: StateFlow<Uri?> get() = _imageAvatarUri

    fun updateImageAvatarUri(newUri: Uri?) {
        viewModelScope.launch {
            _imageAvatarUri.value = newUri
        }
    }

    private val _imageBackgroundUri = MutableStateFlow<Uri?>(null)
    val imageBackgroundUri: StateFlow<Uri?> get() = _imageBackgroundUri

    fun updateImageBackgroundUri(newUri: Uri?) {
        viewModelScope.launch {
            _imageBackgroundUri.value = newUri
        }
    }
}