package com.example.social.data.repository

import com.example.social.data.model.Comment
import com.example.social.domain.usecase.FirestoreMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class CommentRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    private val firestoreMethod = FirestoreMethod(firebaseAuth, firestore)

    fun createCommentDocument(idPost: String) {
        val docRef = firestore.collection("comments").document(idPost)
        // tạo collection comments rỗng khi đăng nhập tài khoản, nếu có rồi thì không tạo nữa
        docRef.set(mapOf<String, Any>(), SetOptions.merge())
    }

    suspend fun getComment(postId: String): Map<String, Any>? {
        val docRef = firestore.collection("comments").document(postId)

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

    suspend fun updateComment(child: String, content: String,
                              postId: String) {
        val userUID = firestoreMethod.fetchInfoData("users", "uid",
            firebaseAuth.currentUser!!.uid).toString()
        val timeStamp = System.currentTimeMillis()
        val commentsRef = firestore.collection("comments").document(postId)
        val comments: Map<String, Any>? = getComment(postId)
        if (comments != null) {
            val commentsCount = comments.size
            val newCmtId = "${child}${commentsCount.plus(1)}"
            val commentModel = Comment(userUID, content, timeStamp)
            commentsRef.update(newCmtId, commentModel)
        }
    }
}