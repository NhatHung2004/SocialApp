package com.example.social.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartFriendSuggestViewModel: ViewModel(){
    private val userRepo = UserRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _allUsers = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val allUsers: StateFlow<List<Map<String, Any>>> get() = _allUsers
    init{
        getAllUsersInfo()
    }
    private fun getAllUsersInfo() {
        viewModelScope.launch {
            val users = userRepo.fetchAllUsers()
            if (users != null) {
                _allUsers.value = users
            }
        }
    }

}