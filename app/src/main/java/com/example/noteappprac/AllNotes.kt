package com.example.noteappprac

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappprac.databinding.ActivityAllNotesBinding
import com.example.noteappprac.databinding.DialogUpdateNoteBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AllNotes : AppCompatActivity(), NotesAdapter.OnItemClickListener {
    private val binding by lazy { ActivityAllNotesBinding.inflate(layoutInflater) }
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val noteReference = database.child("user").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteItems>()
                    for (itemSnapshot in snapshot.children) {
                        val note = itemSnapshot.getValue(NoteItems::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                        val adapter = NotesAdapter(noteList, this@AllNotes)
                        recyclerView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = database.child("user").child(user.uid).child("notes")
            noteReference.child(noteId).removeValue()
        }
    }

    override fun onUpdateClick(noteId: String, currentTitle: String, currentDescription: String) {

        val dialogBinding = DialogUpdateNoteBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Note")
            .setPositiveButton("Update") { dialog, _ ->
                val newTitle = dialogBinding.updateNoteTitle.text.toString()
                val newDescription = dialogBinding.updateNoteDescription.text.toString()
                updateNoteDatabase(newTitle, newDescription, noteId)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialogBinding.updateNoteTitle.setText(currentTitle)
        dialogBinding.updateNoteDescription.setText(currentDescription)

        dialog.show()
    }

    private fun updateNoteDatabase(newTitle: String, newDescription: String, noteId: String) {

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = database.child("user").child(user.uid).child("notes")

            val updateNote = NoteItems(newTitle, newDescription, noteId)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        Toast.makeText(this, "✔", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, "❌", Toast.LENGTH_SHORT).show()
                }
        }
    }
}