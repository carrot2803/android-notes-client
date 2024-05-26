package com.carrot.noteapp.repository

import com.carrot.noteapp.data.local.dao.NoteDao
import com.carrot.noteapp.data.remote.NoteAPI
import com.carrot.noteapp.data.remote.models.User
import com.carrot.noteapp.utils.Result
import com.carrot.noteapp.utils.SessionManager
import com.carrot.noteapp.utils.isNetworkConnected
import javax.inject.Inject

class NoteRepoImpl @Inject constructor(val noteAPI: NoteAPI, val noteDao: NoteDao, val sessionManager: SessionManager) :
    NoteRepo {
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
            Result.Error<String>(e.message ?: "Unexpected Error")
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
            Result.Success("Logged In Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error<String>(e.message ?: "Unexpected Error")
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