package com.example.plantoplant

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.util.ServerCon
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class CalenderAdapter(private val dayList: ArrayList<String>, private val onItemListener: Context):
    RecyclerView.Adapter<CalenderAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val todo1Text: TextView = itemView.findViewById(R.id.todo1)
        val todo2Text: TextView = itemView.findViewById(R.id.todo2)
        val todo3Text: TextView = itemView.findViewById(R.id.todo3)
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
        if (day != "") {
            holder.todo1Text.text = "hello"
            holder.todo2Text.text = "hello"
            holder.todo3Text.text = "hello"
        }
        else {
            holder.todo1Text.text = ""
            holder.todo2Text.text = ""
            holder.todo3Text.text = ""
        }

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

    private fun getCalendarTodo(userId: String): String {
        var response = ""

        try {
            val con = ServerCon()
            val url = URL(con.url + "months")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()
//            jsonObject.put("id", email)
//            jsonObject.put("password", password)

            // 서버로 값 전송
            val outStream = OutputStreamWriter(conn.outputStream, "UTF-8")
            outStream.write(jsonObject.toString())
            outStream.flush()

            // 서버에서 결과 받기
            val inputStream = conn.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append('\n')
            }

            response = stringBuilder.toString()
            Log.d(ContentValues.TAG, "서버에서 받은 결과: $response")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response
    }
}