package com.example.social.data.repository

import com.example.social.domain.usecase.FirestoreMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore


class UserRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    private val firestoreMethod = FirestoreMethod(firebaseAuth, firestore)

    suspend fun fetchUserInfo(field: String): String? {
        return try {
            firestoreMethod.fetchInfoData("users", field)
        }catch (e: Exception) {
            println("Error getting user data: $e")
            null // Trả về null nếu có lỗi
        }
    }

    suspend fun fetchUserInfoFromUid(field:String,uid:String): String? {
        return try {
            firestoreMethod.fetchInfoData("users", field,uid)
        }catch (e: Exception) {
            println("Error getting user data: $e")
            null // Trả về null nếu có lỗi
        }
    }

    suspend fun getUsersFromUserId(userIds:List<String>): List<Map<String, Any>> {
        return try {
            firestoreMethod.fetchUserInfoFromUserId(userIds)
        } catch (e: Exception) {
            println("Error fetching user info: $e")
            emptyList() // Trả về danh sách rỗng nếu có lỗi xảy ra
        }
    }

    suspend fun fetchAllUsers(): List<Map<String, Any>>? {
        return try{
            firestoreMethod.fetchAllUserInfoData("users")
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

    fun updateUserStatus(status:String){
        firestoreMethod.updateData("users","status",status)
    }

}