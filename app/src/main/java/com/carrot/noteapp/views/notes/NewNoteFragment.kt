package com.carrot.noteapp.views.notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.carrot.noteapp.R
import com.carrot.noteapp.databinding.FragmentNewNoteBinding
import com.carrot.noteapp.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewNoteFragment : Fragment(R.layout.fragment_new_note) {
    private var _binding: FragmentNewNoteBinding? = null
    val binding: FragmentNewNoteBinding? get() = _binding

    //rename to noteViewModel not Notes
    val notesViewModel: NotesViewModel by activityViewModels()
    val args: NewNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewNoteBinding.bind(view)

        notesViewModel.oldNote = args.note

        notesViewModel.oldNote?.title?.let {
            binding?.newNoteTitleEditText?.setText(it)
        }

        notesViewModel.oldNote?.description?.let {
            binding?.newNoteDescriptionEditText?.setText(it)
        }

        binding?.date?.isVisible = notesViewModel.oldNote != null

        notesViewModel.oldNote?.date?.let {
            binding?.date?.text = notesViewModel.milliToDate(it)
        }
    }

    override fun onPause() {
        super.onPause()
        if (notesViewModel.oldNote == null)
            createNote()
        else
            updateNote()
    }

    private fun createNote() {
        val noteTitle = binding?.newNoteTitleEditText?.text?.toString()?.trim()
        val description = binding?.newNoteDescriptionEditText?.text?.toString()?.trim()

        if (noteTitle.isNullOrEmpty() && description.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Note is Empty!", Toast.LENGTH_SHORT).show()
            return
        }
        notesViewModel.createNote(noteTitle, description)
    }

    private fun updateNote() {
        val noteTitle = binding?.newNoteTitleEditText?.text?.toString()?.trim()
        val description = binding?.newNoteDescriptionEditText?.text?.toString()?.trim()

        if (noteTitle.isNullOrEmpty() && description.isNullOrEmpty()) {
            notesViewModel.deleteNote(notesViewModel.oldNote!!.noteID)
            return
        }

        notesViewModel.updateNote(noteTitle, description)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}