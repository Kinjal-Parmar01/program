package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "notes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE notes(id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun insertNote(text: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("text", text)
        }
        db.insert("notes", null, values)
    }

    fun updateNote(id: Int, newText: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("text", newText)
        }
        db.update("notes", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteNote(id: Int) {
        writableDatabase.delete("notes", "id=?", arrayOf(id.toString()))
    }

    fun getAllNotes(): ArrayList<Note> {
        val list = ArrayList<Note>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM notes", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val text = cursor.getString(1)
            list.add(Note(id, text))
        }
        cursor.close()
        return list
    }
}
