package com.example.composenotes.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenotes.data.db.dao.NoteDao
import com.example.composenotes.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {
    val notes: StateFlow<List<Note>> = noteDao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNote(content: String) {
        viewModelScope.launch {
            noteDao.insert(Note(content = content))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }
}