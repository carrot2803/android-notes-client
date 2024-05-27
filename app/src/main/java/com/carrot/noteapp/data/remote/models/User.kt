package com.carrot.noteapp.data.remote.models

data class User(
    val email: String,
    val password: String,
    val name: String? = null,
)