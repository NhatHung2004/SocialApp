package com.example.social.data.repository

import com.example.social.data.model.Notification
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class NotificationRepo(private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore) {


    fun createNotificationDocument(child:String) {
        val docRef = firestore.collection(child).document(Firebase.auth.currentUser!!.uid)
        // tạo collection posts rỗng khi đăng nhập tài khoản, nếu có rồi thì không tạo nữa
        docRef.set(mapOf<String, Any>(), SetOptions.merge())
    }

    suspend fun getNotification(userId: String): Map<String, Any>? {
        val docRef = firestore.collection("notifications").document(userId)

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

    suspend fun updateNotification(uidUser:String,uidPost:String, content: String,readState:String,uid:String) {
        val timeStamp = System.currentTimeMillis()
        val nofRef = firestore.collection("notifications").document(uid)
        val notifications: Map<String, Any>? = getNotification(uid)
        if (notifications != null){
            val notificationsCount = notifications.size
            val newNotificationId = "notification${notificationsCount.plus(1)}"
            val notificationModel= Notification(uidUser,uidPost,content,readState,timeStamp)
            nofRef.update(newNotificationId,notificationModel )
        }
    }

    fun updateReadState(idNof:String,readState: String){
        val nofRef=firestore.collection("notifications").document(Firebase.auth.currentUser!!.uid)
        nofRef.update("$idNof.readState",readState)
    }

    suspend fun deleteNotificationRelation(userId1:String,userId2:String){
        val NofsRef = firestore.collection("notifications").document(userId1)
        val document = NofsRef.get().await()

        if(document.exists()){
            val Nofs=document.data
            if(Nofs!=null){
                val friendKey = Nofs.entries.firstOrNull { it.value is Map<*, *> && (it.value as Map<*, *>)["uidUser"] == userId2}?.key

                if(friendKey!=null){
                    val updates= hashMapOf<String,Any>(
                        friendKey to FieldValue.delete()
                    )
                    NofsRef.update(updates).await()

                    val updatedFriends = Nofs.filterKeys { it != friendKey.toString() }
                        .toList()
                        .sortedBy { it.first } // Sắp xếp lại theo key
                        .mapIndexed { index, entry ->
                            "notification${index + 1}" to entry.second
                        }
                        .toMap()
                    NofsRef.set(updatedFriends).await()
                }
            }
        }
    }

    suspend fun deleteNotificationPost(userId1: String, userId2: String, postId: String){
        val NofsRef = firestore.collection("notifications").document(userId1)
        val document = NofsRef.get().await()

        if (document.exists()) {
            val Nofs = document.data
            if (Nofs != null) {
                val friendKey = Nofs.entries.firstOrNull {
                    val value = it.value
                    if (value is Map<*, *>) {
                        val uidUser = value["uidUser"] as? String
                        val currentPostId = value["uidPost"] as? String
                        uidUser == userId2 && currentPostId == postId
                    } else {
                        false
                    }
                }?.key

                if (friendKey != null) {
                    // Xóa thông báo có key tương ứng
                    val updates = hashMapOf<String, Any>(
                        friendKey to FieldValue.delete()
                    )
                    NofsRef.update(updates).await()

                    // Cập nhật danh sách còn lại và sắp xếp lại key
                    val updatedFriends = Nofs.filterKeys { it != friendKey.toString() }
                        .toList()
                        .sortedBy { it.first } // Sắp xếp lại theo key
                        .mapIndexed { index, entry ->
                            "notification${index + 1}" to entry.second
                        }
                        .toMap()

                    NofsRef.set(updatedFriends).await()
                }
            }
        }
    }
}