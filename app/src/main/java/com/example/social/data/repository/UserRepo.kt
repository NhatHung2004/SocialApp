package com.example.social.data.repository

import com.example.social.domain.utils.FirestoreMethod
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
            firestoreMethod.fetchInfoData("users", field, uid)
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
            firestoreMethod.fetchAllInfoData("users")
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

    //Hàm cho admin dùng
    fun updateEmail(email: String, uid: String){
        firestoreMethod.updateData("users","email",email, uid)
    }

    fun updateUserStatus(status:String){
        firestoreMethod.updateData("users","status", status)
    }

    //Hàm cho admin dùng
    fun updateUserDeleted(deleted:String, uid: String){
        firestoreMethod.updateData("users","deleted", deleted, uid)
    }

    //Hàm cho admin dùng
    fun updateUserMode(mode:String, uid: String){
        firestoreMethod.updateData("users","mode", mode, uid)
    }

    fun updateHoTen(ho: String, ten: String ,uid: String){
        firestoreMethod.updateData("users","firstname",ten, uid)
        firestoreMethod.updateData("users", "lastname",ho, uid)
    }
}