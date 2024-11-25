package com.example.social.presentation.viewmodel
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.AuthRepo
import com.example.social.data.repository.PostRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val authRepository = AuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val postRepo = PostRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _registrationState = MutableLiveData<Boolean?>()
    val registrationState: LiveData<Boolean?> = _registrationState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = authRepository.login(email, password)
            if (user != null) {
                _currentUser.postValue(user)
                postRepo.createPostDocument()
            }
        }
    }

    fun setRegistrationState(state: Boolean?) {
        _registrationState.postValue(state)
    }

    fun register(email: String, password: String, ho: String, ten: String, gioiTinh: String, date: String, avatar: String, backgroundAvatar: String) {
        viewModelScope.launch {
            val user = authRepository.register(email, password, ho, ten, gioiTinh, date, avatar, backgroundAvatar)
            if (user != null) {
                // Nếu đăng ký thành công, thông báo trạng thái và chuyển về trang đăng nhập
                setRegistrationState(true)
            } else {
                // Nếu đăng ký thất bại
                setRegistrationState(false)
            }
        }
    }

    fun checkCurrentUser() {
        _currentUser.postValue(authRepository.getCurrentUser())
    }

    fun logout() {
        authRepository.logout()
        _currentUser.postValue(null)
    }
}