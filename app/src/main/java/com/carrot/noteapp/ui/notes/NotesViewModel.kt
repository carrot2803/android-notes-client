package com.carrot.noteapp.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrot.noteapp.data.local.models.LocalNote
import com.carrot.noteapp.repository.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(val noteRepo: NoteRepo) : ViewModel() {
    val notes = noteRepo.getAllNotes()
    var oldNote: LocalNote? = null
    var searchQuery: String = ""

    fun syncNotes(onDone: (() -> Unit)? = null) = viewModelScope.launch {
        noteRepo.syncNotes()
        onDone?.invoke()
    }


    fun createNote(noteTitle: String?, description: String?) = viewModelScope.launch(Dispatchers.IO) {
        val localNote = LocalNote(noteTitle, description)
        noteRepo.createNote(localNote)
    }

    fun deleteNote(noteId: String) = viewModelScope.launch {
        noteRepo.deleteNote(noteId)
    }

    fun undoDelete(note: LocalNote) = viewModelScope.launch {
        noteRepo.createNote(note)
    }

    fun updateNote(noteTitle: String?, description: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (noteTitle == oldNote?.title && description == oldNote?.description && oldNote?.connected == true)
            return@launch
        val note = LocalNote(noteTitle, description, noteID = oldNote!!.noteID)
        noteRepo.updateNote(note)
    }

    fun milliToDate(time: Long): String {
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat("hh:mm a | MMM d, yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

}






