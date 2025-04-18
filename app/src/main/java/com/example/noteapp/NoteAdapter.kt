package com.example.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var notes: ArrayList<Note>,
    private val onEdit: (Note) -> Unit,
    private val onDelete: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.note_text)
        val editBtn: ImageButton = view.findViewById(R.id.btn_edit)
        val deleteBtn: ImageButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.textView.text = note.text
        holder.editBtn.setOnClickListener { onEdit(note) }
        holder.deleteBtn.setOnClickListener { onDelete(note) }
    }

    override fun getItemCount(): Int = notes.size

    fun updateList(newList: ArrayList<Note>) {
        notes = newList
        notifyDataSetChanged()
    }
}
