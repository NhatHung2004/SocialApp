package com.example.social.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.NotificationRepo
import com.example.social.domain.utils.FirestoreMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel: ViewModel(){
    private val firestoreMethod = FirestoreMethod(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val notificationRepo = NotificationRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val _notifications = MutableStateFlow<Map<String, Any>?>(null)


    val notifications: StateFlow<Map<String, Any>?> get() = _notifications

    fun getNotifications(userID: String){
        viewModelScope.launch {
            _notifications.value=notificationRepo.getNotification(userID)
        }
    }

    fun updateNotificationToFireStore(uidUser:String,uidPost:String, content: String,readState:String,uid:String) {
        viewModelScope.launch {
            notificationRepo.updateNotification(uidUser, uidPost, content, readState, uid)
        }
    }

    suspend fun getAvatar(uid: String): String? {
        return firestoreMethod.fetchInfoData("users", "avatar", uid)
    }

    suspend fun getName(uid: String): String {
        val firstname = firestoreMethod.fetchInfoData("users", "firstname", uid)
        val lastname = firestoreMethod.fetchInfoData("users", "lastname", uid)
        return "$firstname $lastname"
    }

    fun updateReadState(idNof:String,readState: String){
        viewModelScope.launch {
            notificationRepo.updateReadState(idNof,readState)
        }
    }
}