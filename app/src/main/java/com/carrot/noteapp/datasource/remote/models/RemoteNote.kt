package com.carrot.noteapp.datasource.remote.models

data class RemoteNote(
    val title: String?,
    val description: String?,
    val date: Long,
    val id: String
)