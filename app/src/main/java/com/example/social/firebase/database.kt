package com.example.social.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.social.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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

    fun signup(email : String, password : String, displayName : String, navController: NavController, context: Context){
        if(email.isNotEmpty() && password.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task->
                    if (task.isSuccessful){
                        Toast.makeText(context, "Create account successfully", Toast.LENGTH_SHORT).show()
                        addUser(task.result.user!!, displayName)
                        navController.navigate(Routes.SIGN_IN)
                    }else{
                        Toast.makeText(context, task.exception?.message?:"Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addUser(user: FirebaseUser, userName: String) {
        val data = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "displayName" to userName,
        )
        val profileUpdates = userProfileChangeRequest {
            displayName = userName
        }
        user.updateProfile(profileUpdates)
        db.collection("users").document(user.uid).set(data)
    }
}