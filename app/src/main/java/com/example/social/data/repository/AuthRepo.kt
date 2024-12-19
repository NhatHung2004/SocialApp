package com.example.social.data.repository

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.social.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email : String, password : String, ho : String, ten : String,
                         gioiTinh : String, date : String, avatar: String,
                         backgroundAvatar: String,status:String, mode: String, deleted: String
    ): FirebaseUser? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {
                val userId = it.uid
                val userEmail = it.email

                // Tạo đối tượng User và lưu vào Firestore
                val userModel = userEmail?.let { it1 ->
                    User(
                        userId,
                        it1,
                        ho,
                        ten,
                        gioiTinh,
                        date,
                        avatar,
                        backgroundAvatar,
                        status, mode, deleted
                    )
                }
                if (userModel != null) {
                    saveUserToFirestore(userModel)
                }
            }
            result.user
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        try {
            // Thêm thông tin người dùng vào Firestore
            val profileUpdates = userProfileChangeRequest {
                displayName = "${user.firstname} ${user.lastname}"
            }
            getCurrentUser()?.updateProfile(profileUpdates)?.await()
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error saving user to Firestore", e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}