package com.example.social.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.CommentRepo
import com.example.social.domain.utils.FirestoreMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel: ViewModel() {
    private val commentRepo = CommentRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val firestoreMethod = FirestoreMethod(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _comments = MutableStateFlow<Map<String, Any>?>(null)
    val comments: StateFlow<Map<String, Any>?> get() = _comments

    fun getComments(postId: String) {
        viewModelScope.launch {
            _comments.value = commentRepo.getComment(postId)
        }
    }

    fun getComment(): Map<String, Any>? {
        return _comments.value
    }

    suspend fun getAvatar(uid: String): String? {
        return firestoreMethod.fetchInfoData("users", "avatar", uid)
    }

    suspend fun getName(uid: String): String {
        val firstname = firestoreMethod.fetchInfoData("users", "firstname", uid)
        val lastname = firestoreMethod.fetchInfoData("users", "lastname", uid)
        return "$firstname $lastname"
    }

    fun updateComment(child: String, content: String, postId: String) {
        viewModelScope.launch {
            commentRepo.updateComment(child, content, postId)
        }
    }
}