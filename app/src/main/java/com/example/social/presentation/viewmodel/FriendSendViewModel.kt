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

class FriendSendViewModel : ViewModel() {
    private val friendRepo = FriendRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _friendSends= MutableStateFlow<Map<String, Any>?>(null)
    private val _userInfos = MutableStateFlow<List<Map<String, Any>>>(emptyList())

    val friendSends: StateFlow<Map<String, Any>?> get() = _friendSends
    val userInfo : StateFlow<List<Map<String, Any>>> get() = _userInfos

    fun getFriendSends(userID: String) {
        viewModelScope.launch {
            _friendSends.value = friendRepo.getFriend(userID,"friendSends")
        }
    }

    fun updateFriendSendToFirestore(userId1:String,userId2:String) {
        viewModelScope.launch {
            friendRepo.updateFriend("friendSends",userId1,userId2)
        }
    }

    fun  deleteFriendSend(userId1:String,userId2:String){
        viewModelScope.launch {
            friendRepo.deleteFriend("friendSends",userId1,userId2)
        }
    }

    fun getFriendInfo(userId: List<String>) {
        viewModelScope.launch {
            val friendInfoList = userRepo.getUsersFromUserId(userId)
            _userInfos.value = friendInfoList
        }
    }
}