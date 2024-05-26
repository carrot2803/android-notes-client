package com.carrot.noteapp.data.remote.models

data class RemoteNote(
    val title: String?,
    val description: String?,
    val date: Long,
    val id: String
)