package com.carrot.noteapp.di

import android.content.Context
import androidx.room.Room
import com.carrot.noteapp.data.local.NoteDatabase
import com.carrot.noteapp.data.local.dao.NoteDao
import com.carrot.noteapp.data.remote.NoteAPI
import com.carrot.noteapp.repository.NoteRepo
import com.carrot.noteapp.repository.NoteRepoImpl
import com.carrot.noteapp.utils.Constants.BASE_URL
import com.google.gson.Gson
import com.carrot.noteapp.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context) = SessionManager(context)

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, "note_db").build()

    @Singleton
    @Provides
    fun provideNoteDao(noteDB: NoteDatabase) = noteDB.getNoteDao()

    @Singleton
    @Provides
    fun provideNoteAPI(): NoteAPI {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideNoteRepo(noteAPI: NoteAPI, noteDao: NoteDao, sessionManager: SessionManager): NoteRepo {
        return NoteRepoImpl(noteAPI, noteDao, sessionManager)
    }
}