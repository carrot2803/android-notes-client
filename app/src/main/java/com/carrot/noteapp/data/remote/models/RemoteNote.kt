package com.carrot.noteapp.data.remote.models

data class RemoteNote (
    val noteTitle: String?,
    val description: String?,
    val date: Long,
    val noteID: Int
)