package com.example.social.data.repository

import android.util.Log
import com.example.social.data.model.Friend
import com.example.social.domain.utils.FirestoreMethod
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FriendRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {
    private val firestoreMethod = FirestoreMethod(firebaseAuth, firestore)

    fun createFriendDocument(child:String) {
        val docRef = firestore.collection(child).document(Firebase.auth.currentUser!!.uid)
        // tạo collection posts rỗng khi đăng nhập tài khoản, nếu có rồi thì không tạo nữa
        docRef.set(mapOf<String, Any>(), SetOptions.merge())
    }

    suspend fun getFriend(userId: String,child:String): Map<String, Any>? {
        val docRef = firestore.collection(child).document(userId)

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

    suspend fun countFriend(userId: String): Int{
        val friends: Map<String, Any>? = getFriend(userId,"friends")
        if (friends != null) {
            return friends.size
        }
        return 0
    }

    suspend fun updateFriend(child: String, userId1:String, userId2:String){
        val timeStamp=System.currentTimeMillis()
        val friendsRef = firestore.collection(child).document(userId1)
        val friends: Map<String, Any>? = getFriend(userId1,child)
        if (friends != null){
            val friendsCount = friends.size
            val newFriendId = "friend${friendsCount.plus(1)}"
            val friendModel = Friend(userId2,timeStamp)
            friendsRef.update(newFriendId, friendModel)
        }
    }

    suspend fun deleteFriend(child:String,userId1:String,userId2:String) {
        val friendsRef = firestore.collection(child).document(userId1)
        val document = friendsRef.get().await()

        if(document.exists()){
            val friends=document.data
            if(friends!=null){
                val friendKey = friends.entries.firstOrNull { it.value is Map<*, *> && (it.value as Map<*, *>)["uid"] == userId2}?.key

                if(friendKey!=null){
                    val updates= hashMapOf<String,Any>(
                        friendKey to FieldValue.delete()
                    )
                    friendsRef.update(updates).await()

                    val updatedFriends = friends.filterKeys { it != friendKey.toString() }
                        .toList()
                        .sortedBy { it.first } // Sắp xếp lại theo key
                        .mapIndexed { index, entry ->
                            "friend${index + 1}" to entry.second
                        }
                        .toMap()
                    friendsRef.set(updatedFriends).await()
                }
            }
        }
    }

    suspend fun deleteDocument(child:String,userId1:String){
        try {
            // Tham chiếu đến document có ID là userId
            val documentRef = firestore.collection(child).document(userId1)

            documentRef.delete().await()
            Log.d("DeleteDocument", "Document đã được xóa")

        } catch (e: Exception) {
            Log.d("DeleteDocument", "Document không tồn tại")
        }
    }

    suspend fun getAllFriendReqs(): List<Map<String, Any>>? {
        return firestoreMethod.fetchAllInfoData("friendReqs")
    }
}