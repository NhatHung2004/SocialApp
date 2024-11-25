package com.example.social.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreMethod(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    suspend fun fetchInfoData(collectionPath: String, field: String): String? {
        return try {
            val docRef = firestore.collection(collectionPath)
                .document(firebaseAuth.currentUser!!.uid)
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

    fun updateData(collections: String, field: String, value: String) {
        val docRef = firestore.collection(collections).document(Firebase.auth.currentUser!!.uid)
        docRef.update(field, value)
    }
}