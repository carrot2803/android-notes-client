package com.carrot.noteapp.data.remote

import com.carrot.noteapp.data.remote.models.RemoteNote
import com.carrot.noteapp.data.remote.models.SimpleResponse
import com.carrot.noteapp.data.remote.models.User
import retrofit2.http.*

interface NoteAPI {
    @Headers("Content-Type: application/json")
    @POST("/register")
    suspend fun createAccount(@Body user: User): SimpleResponse

    @Headers("Content-Type: application/json")
    @POST("/login")
    suspend fun login(@Body user: User): SimpleResponse

    @Headers("Content-Type: application/json")
    @POST("/notes")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote
    ): SimpleResponse


    @Headers("Content-Type: application/json")
    @GET("/notes")
    suspend fun getAllNotes(
        @Header("Authorization") token: String
    ): List<RemoteNote>

    @Headers("Content-Type: application/json")
    @PUT("/notes")
    suspend fun updateNote(
        @Header("Authorization") token:String,
        @Body note:RemoteNote
    ): SimpleResponse


    @Headers("Content-Type: application/json")
    @DELETE("/notes/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
//        @Path("id") noteId: String
    ): SimpleResponse
}
