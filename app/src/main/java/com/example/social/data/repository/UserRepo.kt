package com.example.social.data.repository

import com.example.social.domain.usecase.FirestoreMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore


class UserRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    private val firestoreMethod = FirestoreMethod(firebaseAuth, firestore)

    suspend fun fetchUserInfo(field: String): String? {
        return try {
            firestoreMethod.fetchInfoData("users", field, firebaseAuth.currentUser!!.uid)
        }catch (e: Exception) {
            println("Error getting user data: $e")
            null // Trả về null nếu có lỗi
        }
    }

    fun updateUserInfoToFirestore(firstname: String, lastname: String, email: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = "$firstname $lastname"
        }
        firebaseAuth.currentUser!!.verifyBeforeUpdateEmail(email)
        firebaseAuth.currentUser!!.updateProfile(profileUpdates)
        firestoreMethod.updateData("users", "firstname", firstname)
        firestoreMethod.updateData("users", "lastname", lastname)
        firestoreMethod.updateData("users", "email", email)

    }
}