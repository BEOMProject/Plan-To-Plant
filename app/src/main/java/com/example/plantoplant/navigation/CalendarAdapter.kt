package com.example.plantoplant

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


data class ToDoMonths(var id: Int, var date: LocalDate, var toDo: String, var toDoComplete: Boolean)

class CalenderAdapter(private val dayList: ArrayList<String>,
                      private val todoList: ArrayList<ToDoMonths>,
                      private val onItemListener: Context):
    RecyclerView.Adapter<CalenderAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val todo1Text: TextView = itemView.findViewById(R.id.todo1)
        val todo2Text: TextView = itemView.findViewById(R.id.todo2)
        val todo3Text: TextView = itemView.findViewById(R.id.todo3)
        val todo4Text: TextView = itemView.findViewById(R.id.todo4)
        val todo5Text: TextView = itemView.findViewById(R.id.todo5)
    }

    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item,parent,false)

        return ItemViewHolder(view)
    }

    //데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //날짜 변수에 담기
        var day = dayList[holder.adapterPosition]
        var toDoCount = 0

        var todayTodoList = ArrayList<String>()

        for (todo in todoList) {
            if (todo.date.dayOfMonth.toString() == day) {
                todayTodoList.add(todo.toDo)
            }
        }

        if (day != "" && todayTodoList.size != 0) {
            for (text in todayTodoList) {
                if (toDoCount >= 5) break

                when (toDoCount) {
                    0 -> {
                        holder.todo1Text.text = text
                        toDoCount++
                    }
                    1 -> {
                        holder.todo2Text.text = text
                        toDoCount ++
                    }
                    2 -> {
                        holder.todo3Text.text = text
                        toDoCount ++
                    }
                    3 -> {
                        holder.todo4Text.text = text
                        toDoCount ++
                    }
                    4 -> {
                        holder.todo5Text.text = text
                        toDoCount ++
                    }
                }
            }

            while (toDoCount < 5) {
                when (toDoCount) {
                    0 -> {
                        holder.todo1Text.text = ""
                        holder.todo1Text.setBackgroundColor(Color.WHITE)
                        toDoCount++
                    }
                    1 -> {
                        holder.todo2Text.text = ""
                        holder.todo2Text.setBackgroundColor(Color.WHITE)
                        toDoCount ++
                    }
                    2 -> {
                        holder.todo3Text.text = ""
                        holder.todo3Text.setBackgroundColor(Color.WHITE)
                        toDoCount ++
                    }
                    3 -> {
                        holder.todo4Text.text = ""
                        holder.todo4Text.setBackgroundColor(Color.WHITE)
                        toDoCount ++
                    }
                    4 -> {
                        holder.todo5Text.text = ""
                        holder.todo5Text.setBackgroundColor(Color.WHITE)
                        toDoCount ++
                    }
                }
            }
        }
        else {
            holder.todo1Text.text = ""
            holder.todo2Text.text = ""
            holder.todo3Text.text = ""
            holder.todo4Text.text = ""
            holder.todo5Text.text = ""
            holder.todo1Text.setBackgroundColor(Color.WHITE)
            holder.todo2Text.setBackgroundColor(Color.WHITE)
            holder.todo3Text.setBackgroundColor(Color.WHITE)
            holder.todo4Text.setBackgroundColor(Color.WHITE)
            holder.todo5Text.setBackgroundColor(Color.WHITE)
        }

        holder.dayText.text = day

        //텍스트 색상 지정(토,일)
        if((position +1) % 7 == 0){
            holder.dayText.setTextColor(Color.BLUE)
        }else if(position == 0 || position % 7 == 0){
            holder.dayText.setTextColor(Color.RED)
        }

        //날짜 클릭 이벤트
        /*holder.itemView.setOnClickListener{
            onItemListener.onItemClick(day)
        }

         */
    }

    override fun getItemCount(): Int{
        return dayList.size
    }
}
