package com.example.noteapp


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: NoteDatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var notesList: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = NoteDatabaseHelper(this)

        val inputNote = findViewById<EditText>(R.id.input_note)
        val addButton = findViewById<Button>(R.id.btn_add)
        val searchBox = findViewById<EditText>(R.id.search_box)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        notesList = dbHelper.getAllNotes()
        noteAdapter = NoteAdapter(notesList,
            onEdit = { note -> showEditDialog(note) },
            onDelete = { note ->
                dbHelper.deleteNote(note.id)
                refreshList()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter

        addButton.setOnClickListener {
            val text = inputNote.text.toString().trim()
            if (text.isNotEmpty()) {
                dbHelper.insertNote(text)
                inputNote.text.clear()
                refreshList()
            }
        }

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = filterList(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun refreshList() {
        notesList = dbHelper.getAllNotes()
        noteAdapter.updateList(notesList)
    }

    private fun filterList(query: String) {
        val filtered = notesList.filter {
            it.text.contains(query, ignoreCase = true)
        } as ArrayList<Note>
        noteAdapter.updateList(filtered)
    }

    private fun showEditDialog(note: Note) {
        val editText = EditText(this)
        editText.setText(note.text)

        AlertDialog.Builder(this)
            .setTitle("Edit Note")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val newText = editText.text.toString().trim()
                if (newText.isNotEmpty()) {
                    dbHelper.updateNote(note.id, newText)
                    refreshList()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}