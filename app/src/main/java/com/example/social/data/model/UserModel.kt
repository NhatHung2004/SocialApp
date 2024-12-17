package com.example.social.data.model

data class User(
    val uid: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val sex: String,
    val date: String,
    val avatar: String,
    val backgroundAvatar: String,
    val status:String,
    val mode: String,
    val deleted: String
)