package com.example.social.data.model

data class Post(
    val id: String,
    val content: String,
    val timestamp: Long,
    val imageUris: List<String>
)