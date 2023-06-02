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
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue


data class ToDoMonths(var id: Int, var date: LocalDate, var toDo: String, var toDoComplete: Boolean)

class CalenderAdapter(private val dayList: ArrayList<String>,
                      private val userId: String,
                      private val localDate: LocalDate,
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

        val year = localDate.year
        val month = localDate.month.value
        val selectedDay = localDate.dayOfMonth

        if (day != "") {
            // 할 일 가져오기
            val job = CoroutineScope(Dispatchers.IO).launch {
                val response = async { (getCalendarTodo(userId, year, month, selectedDay)) }
                val jsonResult = response.await()
                val toDoArray = parseJson(jsonResult)
                val textArray = ArrayList<String>()

                for (todo in toDoArray) {
                    if (todo.date.dayOfMonth.toString() == day) {
                        textArray.add(todo.toDo)
                    }
                }

                if (jsonResult == "") {
                    holder.todo1Text.text = ""
                    holder.todo2Text.text = ""
                    holder.todo3Text.text = ""
                    holder.todo4Text.text = ""
                    holder.todo5Text.text = ""
                }

                for (text in textArray) {
                    if (toDoCount >= 5) break

                    // 3글자씩 잘라서 띄울 때 쓰는 코드
//                    var viewText = text
//                    if (text.length >= 5) {
//                        viewText = (viewText[0]).toString() + (viewText[1]).toString() + (viewText[2]).toString() + "..."
//                    }
//                    when (toDoCount) {
//                        0 -> {
//                            holder.todo1Text.text = viewText
//                            toDoCount++
//                        }
//                        1 -> {
//                            holder.todo2Text.text = viewText
//                            toDoCount ++
//                        }
//                        2 -> {
//                            holder.todo3Text.text = viewText
//                            toDoCount ++
//                        }
//                        3 -> {
//                            holder.todo4Text.text = viewText
//                            toDoCount ++
//                        }
//                        4 -> {
//                            holder.todo5Text.text = viewText
//                            toDoCount ++
//                        }
//                    }

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
                            toDoCount++
                        }
                        1 -> {
                            holder.todo2Text.text = ""
                            toDoCount ++
                        }
                        2 -> {
                            holder.todo3Text.text = ""
                            toDoCount ++
                        }
                        3 -> {
                            holder.todo4Text.text = ""
                            toDoCount ++
                        }
                        4 -> {
                            holder.todo5Text.text = ""
                            toDoCount ++
                        }
                    }
                }
            }

            runBlocking {
                job.join()
                job.cancel()
            }
        }
        else {
            holder.todo1Text.text = ""
            holder.todo2Text.text = ""
            holder.todo3Text.text = ""
            holder.todo4Text.text = ""
            holder.todo5Text.text = ""
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

    private fun getCalendarTodo(userId: String, year: Int, month: Int, selectedDay: Int): String {
        var response = ""

        try {
            val con = ServerCon()
            val url = URL(con.url + "calendar/months")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()
            jsonObject.put("year", year)
            jsonObject.put("month", month)
            jsonObject.put("day", selectedDay)
            jsonObject.put("user_id", userId)

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

            println(response)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response
    }

    private fun parseJson(json: String): ArrayList<ToDoMonths> {
        var result = ArrayList<ToDoMonths>()

        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val tdm: JSONObject = jsonArray.getJSONObject(i)
                val toDoMonths = ToDoMonths(
                    tdm.getInt("id"),
                    LocalDate.parse(tdm.getString("date"), DateTimeFormatter.ISO_DATE),
                    tdm.getString("toDo"),
                    tdm.getBoolean("toDoCompleted")
                )

                result.add(toDoMonths)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return result
    }
}