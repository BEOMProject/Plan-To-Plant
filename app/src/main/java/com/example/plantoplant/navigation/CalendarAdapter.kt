package com.example.plantoplant.navigation

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.R
import java.time.LocalDate

data class ToDoMonths(var id: Int, var date: LocalDate, var toDo: String, var toDoComplete: Boolean)

class CalenderAdapter(private val dayList: ArrayList<String>,
                      private val toDoList: ArrayList<ToDoMonths>,
                      private val onItemListener: Context):
    RecyclerView.Adapter<CalenderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val calendarTodoRecyclerView: RecyclerView = itemView.findViewById(R.id.calendarTodoRecyclerView)
    }

    //화면 설정
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.calendar_item, viewGroup,false)

        return ViewHolder(view)
    }

    //데이터 설정
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // 날짜 설정
        var day = dayList[position]
        viewHolder.dayText.text = day

        // 날짜 텍스트 색상 지정(토,일)
        if ((position + 1) % 7 == 0) {
            viewHolder.dayText.setTextColor(Color.BLUE)
        } else if (position % 7 == 0) {
            viewHolder.dayText.setTextColor(Color.RED)
        } else {
            viewHolder.dayText.setTextColor(Color.BLACK)
        }

        // 할 일 목록 설정
        val todayTodoList = ArrayList<String>()
        for (todo in toDoList) {
            if (todo.date.dayOfMonth.toString() == day) {
                todayTodoList.add(todo.toDo)
            }
        }

        viewHolder.calendarTodoRecyclerView.layoutManager = LinearLayoutManager(viewHolder.calendarTodoRecyclerView.context, RecyclerView.VERTICAL, false)
        viewHolder.calendarTodoRecyclerView.adapter = CalendarToDoAdapter(todayTodoList)
    }

    override fun getItemCount(): Int{
        return dayList.size
    }
}
