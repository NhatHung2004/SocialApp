package com.example.social.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.UserRepo
import com.example.social.domain.usecase.ImageProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val imageProcess = ImageProcess(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _imageAvatarUri = MutableStateFlow<Uri?>(null)
    private val _imageBackgroundUri = MutableStateFlow<Uri?>(null)
    private val _firstname = MutableStateFlow<String>("")
    private val _lastname = MutableStateFlow<String>("")
    private val _email = MutableStateFlow<String>("")

    val imageAvatarUri: StateFlow<Uri?> get() = _imageAvatarUri
    val imageBackgroundUri: StateFlow<Uri?> get() = _imageBackgroundUri
    val firstname: StateFlow<String> get() = _firstname
    val lastname: StateFlow<String> get() = _lastname
    val email: StateFlow<String> get() = _email


    init {
        getUserInfo()
        updateImageAvatarUri("avatar")
        updateImageBackgroundUri("backgroundAvatar")
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _firstname.value = userRepo.fetchUserInfo("firstname").toString()
            _lastname.value = userRepo.fetchUserInfo("lastname").toString()
            _email.value = userRepo.fetchUserInfo("email").toString()
        }
    }

    fun updateImageAvatarUri(field: String) {
        viewModelScope.launch {
            _imageAvatarUri.value = imageProcess.getImageFromLocal(field)
        }
    }

    fun updateImageBackgroundUri(field: String) {
        viewModelScope.launch {
            _imageBackgroundUri.value = imageProcess.getImageFromLocal(field)
        }
    }

    fun copyImage(selectedUri: Uri, field: String, imagePath: String, context: Context) {
        viewModelScope.launch {
            imageProcess.copyImage(selectedUri, field, imagePath, context)
        }
    }

    fun updateUserInfo(firstname: String, lastname: String, email: String) {
        viewModelScope.launch {
            userRepo.updateUserInfoToFirestore(firstname, lastname, email)
            _firstname.value = firstname
            _lastname.value = lastname
            _email.value = email
        }
    }
}