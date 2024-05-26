package com.carrot.noteapp.repository

import com.carrot.noteapp.data.local.dao.NoteDao
import com.carrot.noteapp.data.local.models.LocalNote
import com.carrot.noteapp.data.remote.NoteAPI
import com.carrot.noteapp.data.remote.models.RemoteNote
import com.carrot.noteapp.data.remote.models.User
import com.carrot.noteapp.utils.Result
import com.carrot.noteapp.utils.SessionManager
import com.carrot.noteapp.utils.isNetworkConnected
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepoImpl @Inject constructor(val noteAPI: NoteAPI, val noteDao: NoteDao, val sessionManager: SessionManager) :
    NoteRepo {

    override fun getAllNotes(): Flow<List<LocalNote>> = noteDao.getAllNotesOrderedByDate()

    override suspend fun getAllNotesFromServer() {
        try {
            val token = sessionManager.getJwtToken() ?: return
            if (!isNetworkConnected(sessionManager.context))
                return
            val result = noteAPI.getAllNotes("Bearer $token")
            result.forEach { remoteNote ->
                noteDao.insertNote(
                    LocalNote(remoteNote.title, remoteNote.description, remoteNote.date, true, noteID = remoteNote.id)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteNote(noteID: String) {
        try {
            noteDao.deleteNoteLocally(noteID)
            val token = sessionManager.getJwtToken() ?: kotlin.run {
                noteDao.deleteNote(noteID)
                return
            }

            if (!isNetworkConnected(sessionManager.context))
                return

            val response = noteAPI.deleteNote("Bearer $token", noteID)
            if (response.success)
                noteDao.deleteNote(noteID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun createNote(note: LocalNote): Result<String> {
        return try {
            noteDao.insertNote(note)
            val token = sessionManager.getJwtToken()
            if (token == null)
                Result.Success("Note is Saved in Local Database")

            if (!isNetworkConnected(sessionManager.context))
                Result.Error<String>("No Internet Connection!")

            val result = noteAPI.createNote(
                "Bearer $token", RemoteNote(
                    note.title, note.description, note.date, note.noteID
                )
            )

            if (!result.success)
                Result.Error<String>(result.message)
            noteDao.insertNote(note.also { it.connected = true })
            Result.Success("Note Saved Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unexpected Error")
        }
    }

    override suspend fun updateNote(note: LocalNote): Result<String> {
        return try {
            noteDao.insertNote(note)
            val token = sessionManager.getJwtToken()
            if (token == null)
                Result.Success("Note is Saved in Local Database")
            if (!isNetworkConnected(sessionManager.context))
                Result.Error<String>("No Internet Connection!")

            val result = noteAPI.createNote(
                "Bearer $token", RemoteNote(
                    note.title, note.description, note.date, note.noteID
                )
            )
            if (!result.success)
                Result.Error<String>(result.message)
            noteDao.insertNote(note.also { it.connected = true })
            Result.Success("Note Updated Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unexpected Error")
        }
    }


    override suspend fun createUser(user: User): Result<String> {
        return try {
            if (!isNetworkConnected(sessionManager.context))
                Result.Error<String>("No Internet Connection")
            val result = noteAPI.createAccount(user)
            if (!result.success)
                Result.Error<String>(result.message)
            sessionManager.updateSession(result.message, user.email, user.name ?: "")
            Result.Success<String>(result.message)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unexpected Error")
        }

    }

    override suspend fun login(user: User): Result<String> {
        return try {
            if (!isNetworkConnected(sessionManager.context))
                Result.Error<String>("No Internet Connection")
            val result = noteAPI.login(user)
            if (!result.success)
                Result.Error<String>(result.message)
            sessionManager.updateSession(result.message, user.email, user.name ?: "")
            getAllNotesFromServer()
            Result.Success("Logged In Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unexpected Error")
        }
    }

    override suspend fun getUser(): Result<User> {
        return try {
            val email = sessionManager.getCurrentUserEmail()
            val username = sessionManager.getCurrentUserName()
            if (email == null || username == null)
                Result.Error<User>("User not Logged In")
            Result.Success(User(username, email!!, ""))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unexpected Error")
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            sessionManager.logout()
            Result.Success("Logged out Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unexpected error")
        }
    }

}