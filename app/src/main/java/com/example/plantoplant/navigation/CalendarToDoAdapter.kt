package com.example.plantoplant.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.R

class CalendarToDoAdapter(private var todayTodoList: ArrayList<String>) :
    RecyclerView.Adapter<CalendarToDoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val toDoText: TextView = view.findViewById(R.id.todoText)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.calendar_todo_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.toDoText.text = todayTodoList[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = todayTodoList.size
}
