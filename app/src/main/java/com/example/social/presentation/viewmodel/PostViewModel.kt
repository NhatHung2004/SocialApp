package com.example.social.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.data.repository.PostRepo
import com.example.social.domain.utils.FirestoreMethod
import com.example.social.domain.utils.ImageProcess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {
    private val imageProcess = ImageProcess(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val postRepo = PostRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    private val firestoreMethod = FirestoreMethod(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _posts = MutableStateFlow<Map<String, Any>?>(null)
    private val _postsFc = MutableStateFlow<Map<String, Any>?>(null)
    private val _allPosts = MutableStateFlow<List<Map<String, Any>>?>(null)

    val posts: StateFlow<Map<String, Any>?> get() = _posts
    val postsFc: StateFlow<Map<String, Any>?> get() = _postsFc
    val allPosts: StateFlow<List<Map<String, Any>>?> get() = _allPosts

    fun getPosts(userID: String) {
        viewModelScope.launch {
            _posts.value = postRepo.getPost(userID)
        }
    }

    fun getPostsFc(userID: String) {
        viewModelScope.launch {
            _postsFc.value = postRepo.getPost(userID)
        }
    }

    suspend fun countPost(userID: String): Int{
        return postRepo.countPost(userID)
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

    fun convertBitmap(context: Context, imageBitmaps: MutableList<Bitmap?>): MutableList<Uri> {
        return imageProcess.convertBitmapListToUriList(context, imageBitmaps)
    }

    fun updateToFirestore(uris: MutableList<Uri>, text: String, context: Context,onPostCreated: (String?) -> Unit) {
        viewModelScope.launch {
            if (uris.isNotEmpty()) {
                val imageUris = mutableListOf<String>()
                for (uri in uris) {
                    val imagePath = imageProcess.uploadImageToCloudinary(uri, context)
                    imageUris.add(imagePath)
                }
                val postId=postRepo.updatePost( text, imageUris)
                onPostCreated(postId)
            }
        }
    }

    fun updateLiked(postID: String, uid: String, uidLike: String) {
        viewModelScope.launch {
            postRepo.updateLiked(postID, uid, uidLike)
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            _allPosts.value = postRepo.getAllPost()
        }
    }

    suspend fun getReport(uid:String,postID: String): String? {
        return postRepo.getReport(uid,postID)
    }

    suspend fun updateReport(uid:String,postId:String, report:String){
        postRepo.updateReport(uid,postId, report)
    }

    fun deletePost(uid: String,postID: String){
        viewModelScope.launch {
            postRepo.deletePost(uid,postID)
        }
    }

    fun deletePostDocument(uid: String){
        viewModelScope.launch {
            postRepo.deletePostDocument(uid)
        }
    }
}