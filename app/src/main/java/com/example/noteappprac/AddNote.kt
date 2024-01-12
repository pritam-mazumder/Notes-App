package com.example.noteappprac

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteappprac.databinding.ActivityAddNoteBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class AddNote : AppCompatActivity() {
    private val binding by lazy { ActivityAddNoteBinding.inflate(layoutInflater) }
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.saveNoteButton.setOnClickListener {

            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDiscriiption.text.toString()

            currentUser?.let { user ->
                val userReference = database.child("user").child(user.uid).child("notes")
                val noteKey = userReference.push().key

                val noteItem = NoteItems(title, description, noteKey ?: "")
                if (noteKey != null) {
                    database.child("user").child(user.uid).child("notes")
                        .child(noteKey).setValue(noteItem)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful)
                                Toast.makeText(this, "✔", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(this, "❌", Toast.LENGTH_SHORT).show()
                        }
                    binding.editTextTitle.text = null
                    binding.editTextDiscriiption.text = null
                }
            }
        }
    }
}
