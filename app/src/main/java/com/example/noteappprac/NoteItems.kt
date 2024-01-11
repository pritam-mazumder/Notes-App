package com.example.noteappprac

data class NoteItems(val title: String, val description: String) {
    constructor() : this("", "")
}
