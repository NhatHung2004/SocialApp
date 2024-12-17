package com.example.social.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.FriendRepo
import com.example.social.data.repository.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileOfFriendViewModel : ViewModel() {
    private val friendRepo = FriendRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())


    private val _friends= MutableStateFlow<Map<String, Any>?>(null)
    private val _userInfos = MutableStateFlow<List<Map<String, Any>>>(emptyList())

    private val _imageAvatarUri = MutableStateFlow<String?>(null)
    private val _imageBackgroundUri = MutableStateFlow<String?>(null)
    private val _firstname = MutableStateFlow("")
    private val _lastname = MutableStateFlow("")
    private val _email = MutableStateFlow("")

    val imageAvatarUri: StateFlow<String?> get() = _imageAvatarUri
    val imageBackgroundUri: StateFlow<String?> get() = _imageBackgroundUri
    val firstname: StateFlow<String> get() = _firstname
    val lastname: StateFlow<String> get() = _lastname
    val friends: StateFlow<Map<String, Any>?> get() = _friends
    val userInfo : StateFlow<List<Map<String, Any>>> get() = _userInfos


    fun getUserInfoFromId(userId:String) {
        viewModelScope.launch {
            _firstname.value = userRepo.fetchUserInfoFromUid("firstname",userId).toString()
            _lastname.value = userRepo.fetchUserInfoFromUid("lastname",userId).toString()
            _email.value = userRepo.fetchUserInfoFromUid("email",userId).toString()
            _imageAvatarUri.value = userRepo.fetchUserInfoFromUid("avatar",userId).toString()
            _imageBackgroundUri.value = userRepo.fetchUserInfoFromUid("backgroundAvatar",userId).toString()
        }
    }

    fun getFriendOfFriend(userId:String){
        viewModelScope.launch {
            _friends.value = friendRepo.getFriend(userId,"friends")
        }
    }

    fun getFriendOfFriendInfo(userId: List<String>) {
        viewModelScope.launch {
            val friendInfoList = userRepo.getUsersFromUserId(userId)
            _userInfos.value = friendInfoList
        }
    }
}