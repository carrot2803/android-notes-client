package com.carrot.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carrot.noteapp.R
import com.carrot.noteapp.databinding.FragmentNoteItemBinding
import com.carrot.noteapp.datasource.local.models.LocalNote

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(val binding: FragmentNoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<LocalNote>() {
        override fun areItemsTheSame(oldItem: LocalNote, newItem: LocalNote): Boolean {
            return oldItem.noteID == newItem.noteID
        }

        override fun areContentsTheSame(oldItem: LocalNote, newItem: LocalNote): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)
    var notes: List<LocalNote>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(FragmentNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            noteText.isVisible = note.title != null
            noteDescription.isVisible = note.description != null

            note.title?.let {
                noteText.text = it
            }

            note.description?.let {
                noteDescription.text = it
            }

            noteSync.setBackgroundResource(
                if (note.connected)
                    R.drawable.synced
                else
                    R.drawable.not_sync
            )

            root.setOnClickListener {
                onItemClickListener?.invoke(note)
            }

        }
    }

    private var onItemClickListener: ((LocalNote) -> Unit)? = null
    fun setOnItemClickListener(listener: (LocalNote) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = notes.size

}