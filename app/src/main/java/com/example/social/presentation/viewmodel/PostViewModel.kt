package com.example.social.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.PostRepo
import com.example.social.domain.usecase.ImageProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {
    private val imageProcess = ImageProcess(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val postRepo = PostRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _posts = MutableStateFlow<Map<String, Any>?>(null)

    val posts: StateFlow<Map<String, Any>?> get() = _posts

    fun getPosts(userID: String) {
        viewModelScope.launch {
            _posts.value = postRepo.getPost(userID)
        }
    }

    fun updatePostToFirestore(child: String, content: String, imageUris: List<Uri?>) {
        viewModelScope.launch {
            postRepo.updatePost(child, content, imageUris)
        }
    }

    fun savePostImageToInternalStorage(
         imageUris: List<Uri>,
         context: Context,
         child: String,
         postId: String): List<Uri> {
        return imageProcess.saveImageToInternalStorage(
            imageUris, context, child, postId
        )
    }

    fun savePostImagePathToLocal(context: Context, filePath: List<Uri>) {
        viewModelScope.launch {
            imageProcess.saveImagePath(context, filePath)
        }
    }

}