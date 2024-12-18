package com.example.social.data.repository

import android.util.Log
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

    suspend fun countPost(userId: String): Int{
        val posts: Map<String, Any>? = getPost(userId)
        if (posts != null) {
            return posts.size
        }
        return 0
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

    suspend fun updatePost(content: String, imageUris: List<String>):String? {
        val timeStamp = System.currentTimeMillis()
        val userID = Firebase.auth.currentUser!!.uid
        val postsRef = firestore.collection("posts").document(userID)
        val posts: Map<String, Any>? = getPost(firebaseAuth.currentUser!!.uid)
        if (posts != null) {
            val newPostsUri = mutableListOf<String>()
            val liked = listOf<String>()
            for ((index, uri) in imageUris.withIndex()) {
                newPostsUri.add(uri)
            }
            val postId = "${UUID.randomUUID()}_${System.currentTimeMillis()}"
            val postModel = Post(postId, userID, content, timeStamp, newPostsUri, liked, "false")
            postsRef.update(postId, postModel)
            commentRepo.createCommentDocument(postId)
            return postId
        }
        return null
    }

    suspend fun updateLiked(postID: String, uid: String, uidLike: String) {
        val posts: Map<String, Any>? = getPost(uid)
        val postsRef = firestore.collection("posts").document(uid)
        if (posts != null) {
            val post = posts[postID] as? Map<String, Any>
            val liked = post?.get("liked") as List<String>
            if(liked.contains(uidLike)) {
                postsRef.update(
                    "$postID.liked",
                    FieldValue.arrayRemove(uidLike)
                )
            }else {
                postsRef.update(
                    "$postID.liked",
                    FieldValue.arrayUnion(uidLike)
                )
            }
        }
    }

    suspend fun updateReport(uid:String, postId:String, report:String){
        val posts: Map<String, Any>? = getPost(uid)
        val postsRef = firestore.collection("posts").document(uid)
        if (posts != null) {
            postsRef.update(
                "$postId.report", report
            )
        }
    }

    suspend fun getReport(uid: String, postId: String): String? {
        val posts: Map<String, Any>? = getPost(uid)
        if (posts != null) {
            val post = posts[postId] as? Map<String, Any>
            return post?.get("report") as? String
        }
        return null // Trả về null nếu bài viết không tồn tại hoặc không có trường report
    }

    suspend fun deletePost(uid:String,postId:String){
        val postsRef = firestore.collection("posts").document(uid)
        val snapshot = postsRef.get().await()
        val posts=snapshot.data

        if(posts!=null && posts.containsKey(postId)){
            postsRef.update(postId, FieldValue.delete())
        }
    }

    suspend fun deletePostDocument(uid:String) {
        try {
            // Tham chiếu đến document có ID là userId
            val documentRef = firestore.collection("posts").document(uid)

            documentRef.delete().await()
            Log.d("DeleteDocument", "Document đã được xóa")

        } catch (e: Exception) {
            Log.d("DeleteDocument", "Document không tồn tại")
        }
    }
}