package com.example.social.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.CommentRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel: ViewModel() {
    private val commentRepo = CommentRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _comments = MutableStateFlow<Map<String, Any>?>(null)
    val posts: StateFlow<Map<String, Any>?> get() = _comments

    fun getComments(postId: String) {
        viewModelScope.launch {
            _comments.value = commentRepo.getComment(postId)
        }
    }

    fun updateComment(child: String, content: String, postId: String) {
        viewModelScope.launch {
            commentRepo.updateComment(child, content, postId)
        }
    }
}