package com.carrot.noteapp.repository

import com.carrot.noteapp.data.local.models.LocalNote
import com.carrot.noteapp.data.remote.models.User
import com.carrot.noteapp.utils.Result

interface NoteRepo {
    suspend fun createUser(user: User): Result<String>
    suspend fun login(user: User): Result<String>
    suspend fun getUser(): Result<User>
    suspend fun logout(): Result<String>

    suspend fun createNote(note: LocalNote): Result<String>
    suspend fun updateNote(note: LocalNote): Result<String>
}