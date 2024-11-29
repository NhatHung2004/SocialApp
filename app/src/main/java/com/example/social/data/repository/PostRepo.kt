package com.example.social.data.repository

import android.net.Uri
import com.example.social.data.model.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class PostRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    fun createPostDocument() {
        val docRef = firestore.collection("posts").document(Firebase.auth.currentUser!!.uid)
        // tạo collection posts rỗng khi đăng nhập tài khoản, nếu có rồi thì không tạo nữa
        docRef.set(mapOf<String, Any>(), SetOptions.merge())
    }

    suspend fun getPost(userId: String): Map<String, Any>? {
        val docRef = firestore.collection("posts").document(userId)

        return try {
            val document = docRef.get().await()
            if (document.exists()) {
                document.data
            } else {
                null // Tài liệu không tồn tại
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updatePost(child: String, content: String, imageUris: List<Uri?>) {
        val timeStamp = System.currentTimeMillis()
        val postsRef = firestore.collection("posts").document(Firebase.auth.currentUser!!.uid)
        val posts: Map<String, Any>? = getPost(firebaseAuth.currentUser!!.uid)
        if (posts != null){
            val postsCount = posts.size
            val newPostId = "post${postsCount.plus(1)}"
            val newPostsUri = mutableListOf<String>()
            for ((index, uri) in imageUris.withIndex()) {
                newPostsUri.add(uri.toString())
            }
            val postModel = Post(content, timeStamp, newPostsUri)
            postsRef.update(newPostId, postModel)
        }
    }
}