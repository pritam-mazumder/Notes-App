package com.example.noteappprac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappprac.databinding.ListItemBinding

class NotesAdapter(
    private val note: List<NoteItems>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteClick(noteId: String)
        fun onUpdateClick(noteId: String, title: String, description: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = note[position]
        holder.bind(note)
        holder.binding.updateButton.setOnClickListener {
            itemClickListener.onUpdateClick(note.noteId, note.title, note.description)
        }
        holder.binding.deleteButton.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return note.size
    }

    class NoteViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItems) {
            binding.titleTextView.text = note.title
            binding.descriptionTextView.text = note.description
        }
    }
}