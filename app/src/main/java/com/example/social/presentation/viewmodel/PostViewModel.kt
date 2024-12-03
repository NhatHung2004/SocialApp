package com.example.social.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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

    fun convertBitmap(context: Context, imageBitmaps: MutableList<Bitmap?>): MutableList<Uri> {
        return imageProcess.convertBitmapListToUriList(context, imageBitmaps)
    }

    fun saveAndUpdatePostToLocalAndDb(posts: Map<String, Any>?, context: Context,
                                      imageUris: MutableList<Uri>, text: String
    ) {
        viewModelScope.launch {
            if (imageUris.isNotEmpty()) {
                val postId = "post${posts?.size?.plus(1)}"
                val savedImagePaths = imageProcess.saveImageToInternalStorage(
                    imageUris,
                    context,
                    "posts", postId
                )
                imageProcess.saveImagePath(context, savedImagePaths)
                postRepo.updatePost("posts", text, savedImagePaths)
                imageProcess.clearCacheFiles(context)
            }
        }
    }

}