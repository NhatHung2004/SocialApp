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

class FriendViewModel: ViewModel() {
    private val friendRepo = FriendRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _friends= MutableStateFlow<Map<String, Any>?>(null)
    private val _userInfos = MutableStateFlow<List<Map<String, Any>>>(emptyList())

    private val firestoreMethod = FirestoreMethod(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    val friends: StateFlow<Map<String, Any>?> get() = _friends
    val userInfo : StateFlow<List<Map<String, Any>>> get() = _userInfos

    fun getFriends(userID: String) {
        viewModelScope.launch {
            _friends.value = friendRepo.getFriend(userID,"friends")
        }
    }

    suspend fun getFriend(userID: String): Map<String, Any>? {
        // Lấy danh sách bạn bè từ repository
        return friendRepo.getFriend(userID, "friends")
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

    suspend fun countFriend(userID: String): Int{
        return friendRepo.countFriend(userID)
    }

    fun updateFriendToFirestore( userId1:String,userId2:String) {
        viewModelScope.launch {
            friendRepo.updateFriend("friends",userId1,userId2)
        }
    }

    fun  deleteFriend(userId1:String,userId2:String){
        viewModelScope.launch {
            friendRepo.deleteFriend("friends",userId1,userId2)
        }
    }

    fun getFriendInfo(userId: List<String>) {
        viewModelScope.launch {
            val friendInfoList = userRepo.getUsersFromUserId(userId)
            _userInfos.value = friendInfoList
        }
    }

}