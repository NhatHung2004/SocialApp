package com.example.social.repository

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.social.firebase.Database.db
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase

object FirestoreRepo {
    @Composable
    fun getData(collections: String, field: String): String {
        var data by remember { mutableStateOf("") }
        val docRef = db.collection(collections).document(Firebase.auth.currentUser!!.uid)
        docRef.get().addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
            if (documentSnapshot.exists()) {
                data = documentSnapshot.getString(field).toString()
            }
        }
        return data
    }

    fun updateData(collections: String, field: String, value: String) {
        val docRef = db.collection(collections).document(Firebase.auth.currentUser!!.uid)
        docRef.update(field, value)
    }

    fun addUser(user: FirebaseUser, ho: String, ten: String, gioiTinh: String, date: String, avatar: Int, backgroundAvatar: Int) {
        val data = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "firstname" to ho,
            "lastname" to ten,
            "sex" to gioiTinh,
            "date" to date,
            "avatar" to Uri.parse("android.resource://com.example.social/drawable/$avatar"),
            "backgroundAvatar" to Uri.parse("android.resource://com.example.social/drawable/$backgroundAvatar")
        )
        val profileUpdates = userProfileChangeRequest {
            displayName = "$ho $ten"
        }
        user.updateProfile(profileUpdates)
        db.collection("users").document(user.uid).set(data)
    }

    fun updateUser(ho: String, ten: String, email: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = "$ho $ten"
        }
        com.google.firebase.Firebase.auth.currentUser!!.updateProfile(profileUpdates)
        updateData("users", "firstname", ho)
        updateData("users", "lastname", ten)
        Firebase.auth.currentUser?.verifyBeforeUpdateEmail(email)?.addOnSuccessListener { task ->
            updateData("users", "email", email)
        }
    }

    fun addPost(userId:String, content:String, imageUris: List<Uri?>, onSuccess: (String) -> Unit){
        val timeStamp=System.currentTimeMillis()
        val userDocRef = db.collection("users").document(userId)
        userDocRef.get()
            .addOnSuccessListener { document->
                if(document.exists()){
                    val posts = document.get("posts") as? Map<*, *>
                    val postCount=posts?.size?:0
                    val newPostId="post${postCount+1}"
                    val newPost= hashMapOf(
                        "content" to content,
                        "timestamp" to timeStamp,
                        "imageUris" to imageUris
                    )
                    userDocRef.update("posts.$newPostId", newPost).addOnSuccessListener {
                        onSuccess(newPostId)
                    }
                }
            }
    }
}