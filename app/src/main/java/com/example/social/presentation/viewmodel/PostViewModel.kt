package com.example.social.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.PostRepo
import com.example.social.domain.utils.ImageProcess
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

    fun convertBitmap(context: Context, imageBitmaps: MutableList<Bitmap?>): MutableList<Uri> {
        return imageProcess.convertBitmapListToUriList(context, imageBitmaps)
    }

    fun updateToFirestore(uris: MutableList<Uri>, text: String, context: Context) {
        viewModelScope.launch {
            if (uris.isNotEmpty()) {
                val imageUris = mutableListOf<String>()
                for (uri in uris) {
                    val imagePath = imageProcess.uploadImageToCloudinary(uri, context)
                    imageUris.add(imagePath)
                }
                postRepo.updatePost("post", text, imageUris)
            }
        }
    }

    fun updateLiked(postID: String, uid: String, uidLike: String) {
        viewModelScope.launch {
            postRepo.updateLiked(postID, uid, uidLike)
        }
    }
}