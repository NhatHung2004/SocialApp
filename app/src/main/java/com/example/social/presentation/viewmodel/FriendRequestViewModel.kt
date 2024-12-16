package com.example.social.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.FriendRepo
import com.example.social.data.repository.UserRepo
import com.example.social.domain.utils.FirestoreMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendRequestViewModel: ViewModel()  {
    private val friendRepo = FriendRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val firestoreMethod = FirestoreMethod(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _friendRequests= MutableStateFlow<Map<String, Any>?>(null)
    private val _userInfos = MutableStateFlow<List<Map<String, Any>>>(emptyList())

    val friendRequests: StateFlow<Map<String, Any>?> get() = _friendRequests
    val userInfo : StateFlow<List<Map<String, Any>>> get() = _userInfos

    fun getFriendRequests(userID: String) {
        viewModelScope.launch {
            _friendRequests.value = friendRepo.getFriend(userID,"friendReqs")
        }
    }

    fun updateFriendRequestToFirestore(userId1:String, userId2:String) {
        viewModelScope.launch {
            friendRepo.updateFriend("friendReqs",userId1,userId2)
        }
    }

    fun  deleteFriendReq(userId1:String,userId2:String){
        viewModelScope.launch {
            friendRepo.deleteFriend("friendReqs",userId1,userId2)
        }
    }

    fun getFriendInfo(userId: List<String>) {
        viewModelScope.launch {
            val friendInfoList = userRepo.getUsersFromUserId(userId)
            _userInfos.value = friendInfoList
        }
    }

    suspend fun getFirstname(uid: String): String? {
        return firestoreMethod.fetchInfoData("users", "firstname", uid)
    }

    suspend fun getLastname(uid: String): String? {
        return firestoreMethod.fetchInfoData("users", "lastname", uid)
    }

    suspend fun getAvatar(uid: String): String? {
        return firestoreMethod.fetchInfoData("users", "avatar", uid)
    }
}