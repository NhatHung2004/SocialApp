package com.example.social.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.AuthRepo
import com.example.social.data.repository.FriendRepo
import com.example.social.data.repository.PostRepo
import com.example.social.data.repository.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val authRepository = AuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val postRepo = PostRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val friendRepo = FriendRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _registrationState = MutableStateFlow<Boolean?>(null)
    val registrationState: StateFlow<Boolean?> = _registrationState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = authRepository.login(email, password)
            if (user != null) {
                _currentUser.value = user
                userRepo.updateUserStatus("online")
            }
        }
    }

    fun setRegistrationState(state: Boolean?) {
        _registrationState.value = state
    }

    fun register(email: String, password: String, ho: String, ten: String, gioiTinh: String, date: String, avatar: String, backgroundAvatar: String,status:String) {
        viewModelScope.launch {
            val user = authRepository.register(email, password, ho, ten, gioiTinh, date, avatar, backgroundAvatar,status)
            if (user != null) {
                // Nếu đăng ký thành công, thông báo trạng thái và chuyển về trang đăng nhập
                setRegistrationState(true)
                postRepo.createPostDocument()
                friendRepo.createFriendDocument("friends")
                friendRepo.createFriendDocument("friendReqs")
                friendRepo.createFriendDocument("friendSends")
            } else {
                // Nếu đăng ký thất bại
                setRegistrationState(false)
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun setLogOutStatus(){
        userRepo.updateUserStatus("offline")
    }
}