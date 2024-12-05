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

class FriendViewModel: ViewModel() {
    private val friendRepo = FriendRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _friends= MutableStateFlow<Map<String, Any>?>(null)
    private val _friendReqs= MutableStateFlow<Map<String, Any>?>(null)
    private val _friendSends= MutableStateFlow<Map<String, Any>?>(null)
    private val _userInfos = MutableStateFlow<List<Map<String, Any>>>(emptyList())

    val userInfo : StateFlow<List<Map<String, Any>>> get() = _userInfos
    val friends: StateFlow<Map<String, Any>?> get() = _friends
    val friendReqs: StateFlow<Map<String, Any>?> get() = _friendReqs
    val friendSends: StateFlow<Map<String, Any>?> get() = _friendSends

    fun getFriends(userID: String,child:String) {
        viewModelScope.launch {
            _friends.value = friendRepo.getFriend(userID,child)
        }
    }

    fun getFriendReqs(userID: String,child:String) {
        viewModelScope.launch {
            _friendReqs.value = friendRepo.getFriend(userID,child)
        }
    }

    fun getFriendSends(userID: String,child:String) {
        viewModelScope.launch {
            _friendSends.value = friendRepo.getFriend(userID,child)
        }
    }

    fun updateFriendToFireStore(child: String, userId1:String,userId2:String) {
        viewModelScope.launch {
            friendRepo.updateFriend(child,userId1,userId2)
        }
    }

    //Lay thong tin cua friend thong qua uid
    fun getFriendInfo(userId: List<String>) {
        viewModelScope.launch {
            val friendInfoList = userRepo.getUserFromUserId(userId)
            _userInfos.value = friendInfoList
        }
    }

    fun  deleteFriendReq(child:String,userId1:String,userId2:String){
        viewModelScope.launch {
            friendRepo.deleteFriend(child,userId1,userId2)
        }
    }
}