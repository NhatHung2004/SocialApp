package com.example.social.data.model

data class Post(
    val id: String,
    val userID: String,
    val content: String,
    val timestamp: Long,
    val imageUris: List<String>,
    val liked: List<String>
)