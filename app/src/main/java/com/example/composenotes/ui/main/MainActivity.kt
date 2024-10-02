package com.example.composenotes.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composenotes.data.db.database.NoteDatabase
import com.example.composenotes.ui.main.viewmodel.NoteViewModel
import com.example.composenotes.ui.theme.ComposeNotesTheme
import com.example.composenotes.ui.main.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(NoteDatabase.getDatabase(application).noteDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNotesTheme {
                Surface(modifier = Modifier.padding(16.dp)) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "main") {
                        composable("main") { MainScreen(noteViewModel, navController) }
                        composable("addNote") { AddNoteScreen(noteViewModel, navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(noteViewModel: NoteViewModel, navController: NavController) {
    val notes by noteViewModel.notes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addNote") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            notes.forEach { note ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            shape = RectangleShape
                        )
                ) {
                    Text(
                        text = note.content,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddNoteScreen(noteViewModel: NoteViewModel, navController: NavController) {
    var noteContent by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RectangleShape
                ),
            value = noteContent,
            onValueChange = { noteContent = it },
            label = { Text("Note Content") }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (noteContent.isNotBlank()) {
                    noteViewModel.addNote(noteContent)
                    navController.navigateUp()
                } else {
                    Toast.makeText(context, "Note content cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
            Text("Save Note")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeNotesTheme {
        MainScreen(viewModel(), rememberNavController())
    }
}