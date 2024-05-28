package com.carrot.noteapp.datasource.remote.models

data class User(
    val email: String,
    val password: String,
    val username: String? = null,
)