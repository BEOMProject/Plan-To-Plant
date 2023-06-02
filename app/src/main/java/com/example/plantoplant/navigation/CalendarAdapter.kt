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
    RecyclerView.Adapter<CalenderAdapter.ItemViewHolder>() {

//    private lateinit var todayTodoList: ArrayList<String>

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val calendarTodoRecyclerView: RecyclerView = itemView.findViewById(R.id.calendarTodoRecyclerView)
    }

    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item,parent,false)

        return ItemViewHolder(view)
    }

    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // 날짜 설정
        var day = dayList[position]
        holder.dayText.text = day

        // 날짜 텍스트 색상 지정(토,일)
        if ((position + 1) % 7 == 0) {
            holder.dayText.setTextColor(Color.BLUE)
        } else if (position % 7 == 0) {
            holder.dayText.setTextColor(Color.RED)
        } else {
            holder.dayText.setTextColor(Color.BLACK)
        }

        // 할 일 목록 설정
        val todayTodoList = ArrayList<String>()

        for (todo in toDoList) {
            if (todo.date.dayOfMonth.toString() == day) {
                todayTodoList.add(todo.toDo)
            }
        }

        holder.calendarTodoRecyclerView.layoutManager = LinearLayoutManager(holder.calendarTodoRecyclerView.context, RecyclerView.VERTICAL, false)
        holder.calendarTodoRecyclerView.adapter = CalendarToDoAdapter(todayTodoList)
    }

    override fun getItemCount(): Int{
        return dayList.size
    }
}
