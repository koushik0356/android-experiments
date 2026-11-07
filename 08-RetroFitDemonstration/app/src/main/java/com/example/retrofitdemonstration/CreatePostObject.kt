package com.example.retrofitdemonstration

data class CreatePostObject(
    val userId: Int,
    val title: String,
    val body: String
)