package com.example.social.firebase

import android.annotation.SuppressLint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Database {
    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore
}