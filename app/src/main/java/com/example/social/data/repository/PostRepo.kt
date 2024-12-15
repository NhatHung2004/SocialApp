package com.example.social.data.repository

import android.net.Uri
import com.example.social.data.model.Post
import com.example.social.domain.utils.FirestoreMethod
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.UUID

class PostRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    private val commentRepo = CommentRepo(firebaseAuth, firestore)
    private val firestoreMethod = FirestoreMethod(firebaseAuth, firestore)

    fun createPostDocument() {
        val docRef = firestore.collection("posts").document(Firebase.auth.currentUser!!.uid)
        // tạo collection posts rỗng khi bình luận, nếu có rồi thì không tạo nữa
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

    suspend fun getAllPost(): List<Map<String, Any>>? {
        return firestoreMethod.fetchAllInfoData("posts")
    }

    suspend fun updatePost(child: String, content: String, imageUris: List<String>) {
        val timeStamp = System.currentTimeMillis()
        val userID = Firebase.auth.currentUser!!.uid
        val postsRef = firestore.collection("posts").document(userID)
        val posts: Map<String, Any>? = getPost(firebaseAuth.currentUser!!.uid)
        if (posts != null) {
            val postsCount = posts.size
            val newPostId = "${child}${postsCount.plus(1)}"
            val newPostsUri = mutableListOf<String>()
            val liked = listOf<String>()
            for ((index, uri) in imageUris.withIndex()) {
                newPostsUri.add(uri)
            }
            val postId = "${UUID.randomUUID()}_${System.currentTimeMillis()}"
            val postModel = Post(postId, userID, content, timeStamp, newPostsUri, liked)
            postsRef.update(newPostId, postModel)
            commentRepo.createCommentDocument(postId)
        }
    }

    suspend fun updateLiked(postID: String, uid: String, uidLike: String) {
        val posts: Map<String, Any>? = getPost(uid)
        val postsRef = firestore.collection("posts").document(uid)
        if (posts != null) {
            for ((index, entry) in posts.entries.withIndex()) {
                val postData = entry.value as? Map<*, *>
                if (postData != null) {
                    val postId = postData["id"] as String
                    if (postId == postID) {
                        val liked = postData["liked"] as List<String>
                        if(liked.contains(uidLike)) {
                            postsRef.update(
                                "post" + index.plus(1) + ".liked",
                                FieldValue.arrayRemove(uidLike)
                            )
                        } else {
                            postsRef.update(
                                "post" + index.plus(1) + ".liked",
                                FieldValue.arrayUnion(uidLike)
                            )
                        }
                        return
                    }
                }
            }
        }
    }
}