package com.example.plantoplant

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalenderAdapter(private val dayList: ArrayList<String>, private val onItemListener: Context):
    RecyclerView.Adapter<CalenderAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.dayText)
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