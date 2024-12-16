package com.example.social.data.model

data class Notification(
    val uidUser:String,
    val uidPost:String,
    val content:String,
    val readState:String,
    val timestamp: Long
)