package com.example.social.domain.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreMethod(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    suspend fun fetchInfoData(collectionPath: String, field: String, uid: String? = null): String? {
        return try {
            val docRef = if (uid != null) {
                firestore.collection(collectionPath).document(uid)
            } else {
                firestore.collection(collectionPath).document(firebaseAuth.currentUser!!.uid)
            }
            val documentSnapshot = docRef.get().await()
            if (documentSnapshot.exists()) {
                documentSnapshot.getString(field)
            } else {
                null // Tài liệu không tồn tại
            }
        }catch (e: Exception) {
            println("Error getting user data: $e")
            null // Trả về null nếu có lỗi
        }
    }

    suspend fun fetchUserInfoFromUserId(userIds: List<String>): List<Map<String, Any>> {
        return try {
            val users = mutableListOf<Map<String, Any>>()
            for (userId in userIds) {
                val document = firestore.collection("users").document(userId).get().await()
                document.data?.let { users.add(it) }
            }
            users // Trả về danh sách sau khi thêm dữ liệu
        }catch (e: Exception) {
            println("Error getting user data: $e")
            emptyList()
        }
    }

    suspend fun fetchAllInfoData(collectionPath: String): List<Map<String, Any>>? {
        return try{
            val result = firestore.collection(collectionPath).get().await()
            result.documents.map { it.data ?: emptyMap<String, Any>() }
        }catch (e: Exception) {
            println("Error getting user data: $e")
            null // Trả về null nếu có lỗi
        }
    }

    fun updateData(collections: String, field: String, value: String) {
        val docRef = firestore.collection(collections).document(Firebase.auth.currentUser!!.uid)
        docRef.update(field, value)
    }
}