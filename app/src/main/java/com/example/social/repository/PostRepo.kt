package com.example.social.repository

import android.net.Uri
import com.example.social.firebase.Database.db
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions

object PostRepo {

    fun getPost(
        userId: String,
        callback: (Map<String, Any>?) -> Unit
    ) {
        val docRef = db.collection("posts").document(userId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Trả về dữ liệu dưới dạng Map
                    callback(document.data)
                }
            }
    }

    fun createPostDocument() {
        val docRef = db.collection("posts").document(Firebase.auth.currentUser!!.uid)
        docRef.set(mapOf<String, Any>(), SetOptions.merge())
    }

    fun updatePost(child: String, content: String, imageUris: List<Uri?>) {
        val timeStamp = System.currentTimeMillis()
        val postRef = db.collection("posts").document(Firebase.auth.currentUser!!.uid)
        getPost(userId = Firebase.auth.currentUser!!.uid){post ->
            val postCount = post?.size
            val newPostId = "post${postCount?.plus(1)}"
            val subUris = mutableListOf<String>()
            for ((index, uri) in imageUris.withIndex()) {
                val uriName = "image_" + child + "_" + newPostId + "_" + index + ".jpg"
                subUris.add(uriName)
            }
            val newPost = hashMapOf(
                "content" to content,
                "timestamp" to timeStamp,
                "imageUris" to subUris
            )
            postRef.update(newPostId, newPost)
        }
    }
}