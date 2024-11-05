package com.example.social.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavController
import com.example.social.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Database {

    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore

    fun login(email : String, password : String, navController: NavController, context: Context){
        if(email.isNotEmpty() || password.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if (task.isSuccessful){
                        Toast.makeText(context,"Successfully logged in", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.TAC_VU)
                    }else{
                        Toast.makeText(context,task.exception?.message?:"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun signup(email : String, password : String, ho : String, ten : String, gioiTinh : String, date : String, avatar: Int, backgroundAvatar: Int, navController: NavController, context: Context){
        if(email.isNotEmpty() && password.isNotEmpty()){
            val displayName = "$ho $ten"
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful){
                        Toast.makeText(context, "Create account successfully", Toast.LENGTH_SHORT).show()
                        addUser(task.result.user!!, displayName, gioiTinh, date, avatar, backgroundAvatar)
                        navController.navigate(Routes.SIGN_IN)
                    }else{
                        Toast.makeText(context, task.exception?.message?:"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addUser(user: FirebaseUser, userName: String, gioiTinh: String, date: String, avatar: Int, backgroundAvatar: Int) {
        val data = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "displayName" to userName,
            "sex" to gioiTinh,
            "date" to date,
            "photoUri" to Uri.parse("android.resource://com.example.social/drawable/$avatar"),
            "backgroundAvatar" to Uri.parse("android.resource://com.example.social/drawable/$backgroundAvatar")
        )
        val profileUpdates = userProfileChangeRequest {
            displayName = userName
            photoUri = Uri.parse("android.resource://com.example.social/drawable/$avatar")
        }
        user.updateProfile(profileUpdates)
        db.collection("users").document(user.uid).set(data)
    }
}