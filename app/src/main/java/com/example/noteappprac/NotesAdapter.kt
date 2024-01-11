package com.example.noteappprac

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappprac.databinding.ListItemBinding

class NotesAdapter(private val note: List<NoteItems>) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = note[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return note.size
    }

    class NoteViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItems) {
            binding.titleET.text = note.title
            binding.descriptionET.text = note.description
        }
    }
}